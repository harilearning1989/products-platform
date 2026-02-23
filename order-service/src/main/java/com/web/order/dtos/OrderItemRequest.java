package com.web.order.dtos;

import java.math.BigDecimal;

public record OrderItemRequest(
        Long productId,
        Integer quantity,
        BigDecimal price
) {}
