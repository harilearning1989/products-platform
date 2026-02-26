package com.web.order.services;


import com.web.order.dtos.CreateOrderRequest;
import com.web.order.dtos.OrderResponse;

public interface OrderService {

    //OrderItemResponse createNewOrderOld(OrderItemRequest request);

    OrderResponse createNewOrder(CreateOrderRequest request);
}
