package com.web.order.mappers;

import com.product.dtos.*;
import com.web.order.models.OrderItem;
import com.web.order.models.OrderProduct;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public List<OrderResponse> toResponseList(List<OrderProduct> orders) {
        return orders.stream()
                .map(this::toResponse)
                .toList();
    }

    /* =========================
       OrderItem Mapping
       ========================= */

    public OrderItemResponse toItemResponse(OrderItem item) {
        return new OrderItemResponse(
                item.getId(),
                item.getProductId(),
                item.getProductName(),
                item.getPrice(),
                item.getQuantity(),
                item.getLineTotal()
        );
    }

    public List<OrderItemResponse> toItemResponseList(List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            return List.of();
        }

        return items.stream()
                .map(this::toItemResponse)
                .toList();
    }

    /* =========================
       Order Mapping
       ========================= */

    @Override
    public OrderResponse toResponse(OrderProduct order) {
        return new OrderResponse(
                order.getId(),
                order.getUserId(),
                order.getCustomerEmail(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getCreatedAt(),
                order.getUpdatedAt(),
                toItemResponseList(order.getItems())
        );
    }

    @Override
    public OrderDetailsResponse getOrderDetailsResponse(OrderProduct orderProduct,
                                                        CustomerResponse customer,
                                                        List<OrderDetailItemResponse> itemDetails,
                                                        PaymentResponse payment) {
        return new OrderDetailsResponse(
                orderProduct.getId(),
                orderProduct.getUserId(),
                orderProduct.getStatus(),
                orderProduct.getTotalAmount(),
                customer,
                itemDetails,
                payment
        );
    }

    public OrderItem buildOrderItem(
            OrderItemRequest requestItem,
            ProductResponse product,
            OrderProduct order) {

        BigDecimal itemTotal =
                product.price()
                        .multiply(BigDecimal.valueOf(requestItem.quantity()));

        OrderItem orderItem = new OrderItem();
        orderItem.setProductId(product.id());
        orderItem.setProductName(product.name());
        orderItem.setPrice(product.price());
        orderItem.setQuantity(requestItem.quantity());
        orderItem.setLineTotal(itemTotal);
        orderItem.setOrder(order);

        return orderItem;
    }

}
