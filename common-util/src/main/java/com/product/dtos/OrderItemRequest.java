package com.product.dtos;

public record OrderItemRequest(
        Long productId,
        Integer quantity
) {}
