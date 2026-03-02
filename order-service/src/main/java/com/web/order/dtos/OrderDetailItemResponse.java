package com.web.order.dtos;

import java.math.BigDecimal;

public record OrderDetailItemResponse(
        Long productId,
        String productName,
        BigDecimal price,
        Integer quantity) {
}
