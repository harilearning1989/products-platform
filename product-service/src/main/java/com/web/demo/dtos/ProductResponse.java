package com.web.demo.dtos;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductResponse(
        Long id,
        String sku,
        String name,
        String brand,
        String category,
        BigDecimal price,
        Integer weightInGrams,
        Boolean active,
        String description,
        Instant createdAt,
        Instant updatedAt
) {}
