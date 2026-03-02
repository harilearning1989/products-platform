package com.web.order.services;

import com.web.order.dtos.*;
import com.web.order.enums.OrderStatus;
import com.web.order.mappers.OrderMapper;
import com.web.order.models.OrderItem;
import com.web.order.models.OrderProduct;
import com.web.order.repos.OrderProductRepository;
import com.web.order.wrapper.CustomerClientWrapper;
import com.web.order.wrapper.PaymentClientWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderProductRepository orderProductRepository;
    private final CustomerClientWrapper customerClientWrapper;
    private final PaymentClientWrapper paymentClientWrapper;
    private final ApplicationEventPublisher eventPublisher;
    private final OrderMapper orderMapper;
    private final ProductEnrichmentService productEnrichmentService;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy")
                    .withZone(ZoneOffset.UTC);

    @Override
    @Transactional
    public OrderResponse createNewOrder(CreateOrderRequest request) {
        List<Long> ids = request.items()
                .stream()
                .map(OrderItemRequest::productId)
                .toList();

        Map<Long, ProductResponse> productMap =
                productEnrichmentService.fetchProductMap(ids);

        OrderProduct order = OrderProduct.builder()
                .userId(request.userId())
                .customerEmail(request.customerEmail())
                .status(OrderStatus.PENDING)
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest item : request.items()) {

            ProductResponse product =
                    productMap.get(item.productId());

            if (product == null || !product.active()) {
                throw new RuntimeException(
                        "Invalid product: " + item.productId());
            }

            OrderItem orderItem =
                    buildOrderItem(item, product, order);

            total = total.add(orderItem.getLineTotal());

            order.getItems().add(orderItem);
        }

        order.setTotalAmount(total);

        order = orderProductRepository.save(order);

        eventPublisher.publishEvent(
                new OrderCreatedDomainEvent(order.getId()));

        return orderMapper.toResponse(order);
    }

    @Override
    public OrderDetailsResponse getOrderDetails(Long orderId) {

        OrderProduct orderProduct =
                orderProductRepository.findById(orderId)
                        .orElseThrow(() -> new RuntimeException("Order not found"));

        CustomerResponse customer =
                customerClientWrapper.getCustomerById(orderProduct.getUserId());

        PaymentResponse payment =
                paymentClientWrapper.getPaymentByOrderId(orderId);

        List<Long> productIds =
                orderProduct.getItems()
                        .stream()
                        .map(OrderItem::getProductId)
                        .toList();

        Map<Long, ProductResponse> productMap =
                productEnrichmentService.fetchProductMap(productIds);

        List<OrderDetailItemResponse> itemDetails =
                mapToDetailItems(orderProduct.getItems(), productMap);

        return new OrderDetailsResponse(
                orderProduct.getId(),
                orderProduct.getStatus(),
                orderProduct.getTotalAmount(),
                customer,
                itemDetails,
                payment
        );
    }

    private OrderItem buildOrderItem(
            OrderItemRequest requestItem,
            ProductResponse product,
            OrderProduct order) {

        BigDecimal itemTotal =
                product.price()
                        .multiply(BigDecimal.valueOf(requestItem.quantity()));

        OrderItem orderItem = new OrderItem();
        orderItem.setProductId(product.id());
        orderItem.setProductName(product.name());
        orderItem.setPrice(product.price());
        orderItem.setQuantity(requestItem.quantity());
        orderItem.setLineTotal(itemTotal);
        orderItem.setOrder(order);

        return orderItem;
    }

    private List<OrderDetailItemResponse> mapToDetailItems(
            List<OrderItem> orderItems,
            Map<Long, ProductResponse> productMap) {

        return orderItems.stream()
                .map(item -> {

                    ProductResponse product =
                            productMap.get(item.getProductId());

                    return new OrderDetailItemResponse(
                            item.getProductId(),
                            product != null ? product.name() : item.getProductName(),
                            item.getPrice(),
                            item.getQuantity()
                    );
                })
                .toList();
    }

    /*
    @Transactional
    public OrderResponse createNewOrder(CreateOrderRequest request) {

        // 1️⃣ Bulk fetch products
        List<Long> ids = request.items()
                .stream()
                .map(OrderItemRequest::productId)
                .toList();

        List<ProductResponse> products =
                productClientWrapper.fetchProducts(ids);

        Map<Long, ProductResponse> productMap =
                products.stream()
                        .collect(Collectors.toMap(
                                ProductResponse::id,
                                p -> p
                        ));

        BigDecimal total = BigDecimal.ZERO;

        OrderProduct order = OrderProduct.builder()
                .userId(request.userId())
                .customerEmail(request.customerEmail())
                .status(OrderStatus.PENDING)
                .build();

        for (OrderItemRequest item : request.items()) {

            ProductResponse product =
                    productMap.get(item.productId());

            if (product == null || !product.active()) {
                throw new RuntimeException(
                        "Invalid product: " + item.productId());
            }

            BigDecimal itemTotal =
                    product.price()
                            .multiply(BigDecimal.valueOf(item.quantity()));

            total = total.add(itemTotal);

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(product.id());
            orderItem.setProductName(product.name());
            orderItem.setPrice(product.price());
            orderItem.setQuantity(item.quantity());
            orderItem.setLineTotal(itemTotal);
            orderItem.setOrder(order);

            order.getItems().add(orderItem);
        }

        order.setTotalAmount(total);

        order = orderProductRepository.save(order);

        // 🔥 Publish INTERNAL domain event
        eventPublisher.publishEvent(
                new OrderCreatedDomainEvent(order.getId()));

        return orderMapper.toResponse(order);
    }

    public OrderDetailsResponse getOrderDetails(Long orderId) {
        // 1️⃣ Get Order
        OrderProduct orderProduct = orderProductRepository
                .findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // 2️⃣ Get Customer
        CustomerResponse customer =
                customerClientWrapper.getCustomerById(orderProduct.getUserId());

        // 3️⃣ Get Payment
        PaymentResponse payment =
                paymentClientWrapper.getPaymentByOrderId(orderId);

        // 4️⃣ Get Product Details (bulk)
        List<Long> productIds =
                orderProduct.getItems()
                        .stream()
                        .map(OrderItem::getProductId)
                        .toList();

        List<ProductResponse> products =
                productClientWrapper.fetchProducts(productIds);

        Map<Long, ProductResponse> productMap =
                products.stream()
                        .collect(Collectors.toMap(
                                ProductResponse::id,
                                p -> p
                        ));

        // 5️⃣ Build Final Response
        List<OrderDetailItemResponse> itemDetails =
                orderProduct.getItems()
                        .stream()
                        .map(item -> {

                            ProductResponse product =
                                    productMap.get(item.getProductId());

                            return new OrderDetailItemResponse(
                                    item.getProductId(),
                                    product.name(),
                                    item.getPrice(),
                                    item.getQuantity()
                            );
                        })
                        .toList();

        return new OrderDetailsResponse(
                orderProduct.getId(),
                orderProduct.getStatus(),
                orderProduct.getTotalAmount(),
                customer,
                itemDetails,
                payment
        );
    }*/

    @Override
    public List<OrderResponse> findAllOrders() {
        List<OrderProduct> allOrders = orderProductRepository.findAll();
        return orderMapper.toResponseList(allOrders);
    }

}
