package com.web.inventory.dtos;

public record ProductCreatedEvent(
        Long productId,
        String productName
) {}
