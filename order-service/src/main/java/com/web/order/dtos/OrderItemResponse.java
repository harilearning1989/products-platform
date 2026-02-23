package com.web.order.dtos;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long productId,
        Integer quantity,
        BigDecimal price
) {}
