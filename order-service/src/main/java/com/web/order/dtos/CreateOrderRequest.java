package com.web.order.dtos;

import java.util.List;

public record CreateOrderRequest(
        String customerEmail,
        List<OrderItemRequest> items
) {}
