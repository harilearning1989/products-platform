package com.web.order.services;


import com.product.dtos.CreateOrderRequest;
import com.product.dtos.OrderDetailsResponse;
import com.product.dtos.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse createNewOrder(CreateOrderRequest request);

    OrderDetailsResponse getOrderDetails(Long id);

    List<OrderDetailsResponse> getAllOrderDetails();

    List<OrderDetailsResponse> getAllOrderDetails(String status);

    List<OrderDetailsResponse> getAllOrdersByUserId(Long userId);
}
