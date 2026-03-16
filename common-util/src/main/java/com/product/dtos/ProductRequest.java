package com.product.dtos;

import java.math.BigDecimal;

public record ProductRequest(
        Long productId,
        String sku,
        String name,
        String brand,
        String category,
        BigDecimal price,
        Integer weightInGrams,
        String description
) {}
