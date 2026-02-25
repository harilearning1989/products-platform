package com.web.order.services;

import com.web.order.client.InventoryClient;
import com.web.order.client.ProductClient;
import com.web.order.dtos.*;
import com.web.order.enums.OrderStatus;
import com.web.order.models.OrderProduct;
import com.web.order.producer.OrderEventProducer;
import com.web.order.repos.OrderProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderEventProducer producer;
    private final OrderProductRepository productRepository;
    private final ProductClient productClient;
    private final InventoryClient inventoryClient;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy")
                    .withZone(ZoneOffset.UTC);

    @Override
    public OrderItemResponse createNewOrder(OrderItemRequest request) {
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

        if (!response.success()) {
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

        //orderProduct = productRepository.save(orderProduct);
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

    }

}
