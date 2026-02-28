package com.web.payment.dtos;

public record OrderItemEvent(
        Long productId,
        Integer quantity
) {}