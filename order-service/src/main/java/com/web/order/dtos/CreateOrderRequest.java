package com.web.order.dtos;

import java.util.List;

public record CreateOrderRequest(
        Long userId,
        String customerEmail,
        List<OrderItemRequest> items
) {}
