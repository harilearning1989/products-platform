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
import java.util.*;
import java.util.stream.Collectors;

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
        Set<Long> productIds = productEnrichmentService.getProductIds(request.items());
        Map<Long, ProductResponse> productMap =
                productEnrichmentService.fetchProductMap(productIds);

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

            OrderItem orderItem = orderMapper.buildOrderItem(item, product, order);

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
    @Transactional(readOnly = true)
    public List<OrderDetailsResponse> getAllOrderDetails(String status) {
        OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
        List<OrderProduct> orders = orderProductRepository.findByStatusWithItems(orderStatus);
        return buildOrderDetailsResponse(orders);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDetailsResponse> getAllOrdersByUserId(Long userId) {
        List<OrderProduct> orders = orderProductRepository.findByUserId(userId);
        return buildOrderDetailsResponse(orders);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDetailsResponse> getAllOrderDetails() {
        List<OrderProduct> orders = orderProductRepository.findAll();
        return buildOrderDetailsResponse(orders);
    }

    private List<OrderDetailsResponse> buildOrderDetailsResponse(List<OrderProduct> orders) {
        if (orders.isEmpty()) {
            return Collections.emptyList();
        }

        // 1️⃣ Collect IDs
        List<Long> userIds = orders.stream()
                .map(OrderProduct::getUserId)
                .distinct()
                .toList();

        List<Long> orderIds = orders.stream()
                .map(OrderProduct::getId)
                .toList();

        Set<Long> productIds = orders.stream()
                .flatMap(order -> order.getItems().stream())
                .map(OrderItem::getProductId)
                .collect(Collectors.toSet());

        // 2️⃣ Fetch external data
        List<CustomerResponse> allCustomers = customerClientWrapper.getCustomersBulk(userIds);

        Map<Long, CustomerResponse> customerMap = allCustomers.stream()
                .collect(Collectors.toMap(CustomerResponse::userId, p -> p));

        List<PaymentResponse> allPayments = paymentClientWrapper.getPaymentsBulk(orderIds);

        Map<Long, PaymentResponse> paymentMap = allPayments.stream()
                .collect(Collectors.toMap(PaymentResponse::orderId, p -> p));

        Map<Long, ProductResponse> productMap =
                productEnrichmentService.fetchProductMap(productIds);

        // 3️⃣ Build response
        return orders.stream()
                .map(order -> {

                    CustomerResponse customer = customerMap.get(order.getUserId());

                    PaymentResponse payment = paymentMap.getOrDefault(
                            order.getId(),
                            PaymentResponse.empty(order.getId())
                    );

                    List<OrderDetailItemResponse> itemDetails =
                            mapToDetailItems(order.getItems(), productMap);

                    return orderMapper.getOrderDetailsResponse(
                            order,
                            customer,
                            itemDetails,
                            payment
                    );
                })
                .toList();
    }

    @Override
    public OrderDetailsResponse getOrderDetails(Long orderId) {
        OrderProduct orderProduct =
                orderProductRepository.findById(orderId)
                        .orElseThrow(() -> new RuntimeException("Order not found"));

        CustomerResponse customer = customerClientWrapper.getCustomerById(orderProduct.getUserId());

        PaymentResponse payment = paymentClientWrapper.getPaymentByOrderId(orderId);

        Set<Long> productIds = productEnrichmentService.getProductIdFromEntity(orderProduct.getItems());

        Map<Long, ProductResponse> productMap =
                productEnrichmentService.fetchProductMap(productIds);

        List<OrderDetailItemResponse> itemDetails =
                mapToDetailItems(orderProduct.getItems(), productMap);

        return orderMapper.getOrderDetailsResponse(orderProduct, customer, itemDetails, payment);
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

}
