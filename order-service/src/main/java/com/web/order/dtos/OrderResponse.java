package com.web.order.dtos;

import com.web.order.enums.OrderStatus;

import java.math.BigDecimal;

public record OrderResponse(
        Long orderId,
        BigDecimal totalAmount,
        OrderStatus status
) {}
