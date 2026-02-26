package com.web.order.dtos;

public record OrderItemEvent(
        Long productId,
        Integer quantity
) {}