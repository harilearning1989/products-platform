package com.web.payment.dtos;

import java.math.BigDecimal;

public record OrderItemEvent(
        Long productId,
        Integer quantity,
        BigDecimal price
) {}
