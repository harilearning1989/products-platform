package com.web.order.services;


import com.web.order.dtos.CreateOrderRequest;
import com.web.order.dtos.OrderDetailsResponse;
import com.web.order.dtos.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse createNewOrder(CreateOrderRequest request);

    List<OrderResponse> findAllOrders();

    OrderDetailsResponse getOrderDetails(Long id);
}
