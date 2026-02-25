package com.web.order.dtos;

import com.web.order.enums.OrderStatus;

public record OrderItemResponse(
        Long orderId,
        Long userId,
        Long productId,
        Integer quantity,
        Double totalAmount,
        OrderStatus orderStatus,
        String createdAt,
        String updatedAt
) {}
