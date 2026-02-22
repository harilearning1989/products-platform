package com.web.demo.dtos;

import java.math.BigDecimal;

public record ProductRequest(
        String sku,
        String name,
        String brand,
        String category,
        BigDecimal price,
        Integer weightInGrams,
        String description
) {}
