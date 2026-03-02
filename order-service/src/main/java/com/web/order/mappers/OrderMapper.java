package com.web.order.mappers;

import com.web.order.dtos.OrderResponse;
import com.web.order.models.OrderProduct;

import java.util.List;

public interface OrderMapper {
    List<OrderResponse> toResponseList(List<OrderProduct> allOrders);
    OrderResponse toResponse(OrderProduct allOrders);
}
