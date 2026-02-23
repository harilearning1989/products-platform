package com.web.order.dtos;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponse(
        Long id,
        String customerEmail,
        String status,
        BigDecimal totalAmount,
        Instant createdAt,
        List<OrderItemResponse> items
) {}
