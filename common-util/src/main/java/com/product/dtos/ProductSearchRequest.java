package com.product.dtos;

import java.math.BigDecimal;

public record ProductSearchRequest(
        String category,
        String brand,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        String keyword,
        Boolean active
) {
}
