package com.web.order.services;


import com.web.order.dtos.CreateOrderRequest;
import com.web.order.dtos.OrderResponse;

public interface OrderService {

    OrderResponse createOrder(CreateOrderRequest request);

    OrderResponse getOrder(Long id);
}
