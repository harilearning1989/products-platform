package com.web.order.services;

import com.web.order.dtos.*;
import com.web.order.enums.OrderStatus;
import com.web.order.models.OrderItem;
import com.web.order.models.OrderProduct;
import com.web.order.repos.OrderProductRepository;
import com.web.order.wrapper.ProductClientWrapper;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderProductRepository orderProductRepository;
    private final ProductClientWrapper productClientWrapper;
    private final ApplicationEventPublisher eventPublisher;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy")
                    .withZone(ZoneOffset.UTC);

    @Override
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

        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus()
        );
    }

    /*@Transactional
    public OrderItemResponse createNewOrderOld(OrderItemRequest request) {
        OrderProduct orderProduct = OrderProduct.builder()
                .userId(request.userId())
                .productId(request.productId())
                .quantity(request.quantity())
                .status(OrderStatus.CREATED)   // optional (already set in @PrePersist)
                .build();

        orderProduct = productRepository.save(orderProduct);

        orderEventProducer.publishOrderCreated(orderProduct.getId(),
                request.productId(),
                request.quantity());

        return mapToRecord(orderProduct);
    }

    //@Override
    public OrderItemResponse createNewOrderTmp(OrderItemRequest request) {
        // 1️⃣ Validate product
        ProductResponse product = productClient.getProduct(request.productId());

        System.out.println(product.toString());

        if (product == null || !product.active()) {
            throw new RuntimeException("Product not available");
        }

        // 2️⃣ Calculate total amount
        BigDecimal totalAmount = product.price()
                .multiply(BigDecimal.valueOf(request.quantity()));

        // 3️⃣ Reserve inventory
        ReserveRequest reserveRequest = new ReserveRequest(request.productId(), request.quantity());
        ReserveResponse response =
                inventoryClient.reserveStock(reserveRequest);

        if (!response.reserved()) {
            throw new RuntimeException("Stock not available");
        }

        // 4️⃣ Save order
        OrderProduct orderProduct = OrderProduct.builder()
                .userId(request.userId())
                .productId(request.productId())
                .quantity(request.quantity())
                .amount(totalAmount)
                .status(OrderStatus.CREATED)   // optional (already set in @PrePersist)
                .build();

        orderProduct = productRepository.save(orderProduct);
        return mapToRecord(orderProduct);
    }

    private OrderItemResponse mapToRecord(OrderProduct orderProduct) {
        return new OrderItemResponse(
                orderProduct.getId(),
                orderProduct.getUserId(),
                orderProduct.getProductId(),
                orderProduct.getQuantity(),
                orderProduct.getAmount(),
                orderProduct.getStatus(),
                FORMATTER.format(orderProduct.getCreatedAt()),
                FORMATTER.format(orderProduct.getUpdatedAt())
        );

    }*/

}
