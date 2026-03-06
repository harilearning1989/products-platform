package com.web.order.mappers;

import com.product.dtos.*;
import com.web.order.models.OrderItem;
import com.web.order.models.OrderProduct;

import java.util.List;

public interface OrderMapper {
    List<OrderResponse> toResponseList(List<OrderProduct> allOrders);

    OrderResponse toResponse(OrderProduct allOrders);

    OrderDetailsResponse getOrderDetailsResponse(
            OrderProduct orderProduct,
            CustomerResponse customer,
            List<OrderDetailItemResponse> itemDetails,
            PaymentResponse payment);

    OrderItem buildOrderItem(
            OrderItemRequest requestItem,
            ProductResponse product,
            OrderProduct order);
}
