package com.product.dtos;

public record OrderItemEvent(
        Long productId,
        Integer quantity
) {}