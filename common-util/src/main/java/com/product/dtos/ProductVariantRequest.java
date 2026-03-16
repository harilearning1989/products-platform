package com.product.dtos;

import java.math.BigDecimal;

public record ProductVariantRequest(
        String size,
        String color,
        String configuration,
        BigDecimal price,
        Integer stock
) {
}
