package com.web.inventory.dtos;

import java.time.Instant;

public record InventoryDto(
        Long id,
        Long productId,
        Integer availableQuantity,
        Integer reservedQuantity,
        Instant lastUpdated
) {}
