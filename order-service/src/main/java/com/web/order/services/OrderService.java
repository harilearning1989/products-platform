package com.web.order.services;


import com.web.order.dtos.OrderItemRequest;
import com.web.order.dtos.OrderItemResponse;

public interface OrderService {

    OrderItemResponse createNewOrder(OrderItemRequest request);
}
