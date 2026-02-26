package com.web.order.dtos;

import com.web.order.enums.OrderStatus;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long orderId,
        Long userId,
        Long productId,
        Integer quantity,
        BigDecimal totalAmount,
        OrderStatus orderStatus,
        String createdAt,
        String updatedAt
) {}
