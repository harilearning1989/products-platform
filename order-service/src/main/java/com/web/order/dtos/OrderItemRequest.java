package com.web.order.dtos;

public record OrderItemRequest(
        Long userId,
        Long productId,
        Integer quantity
) {}
