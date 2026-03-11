package com.product.dtos;

import com.product.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponse(
        Long orderId,
        Long userId,
        String customerEmail,
        BigDecimal totalAmount,
        OrderStatus status,
        Instant createdAt,
        Instant updatedAt,
        List<OrderItemResponse> items
) {}
