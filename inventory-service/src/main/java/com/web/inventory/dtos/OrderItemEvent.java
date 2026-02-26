package com.web.inventory.dtos;

public record OrderItemEvent(
        Long productId,
        Integer quantity
) {}