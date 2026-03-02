package com.web.order.dtos;

import com.web.order.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

public record OrderDetailsResponse(Long orderId,
                                   OrderStatus status,
                                   BigDecimal totalAmount,
                                   CustomerResponse customerResponse,
                                   List<OrderDetailItemResponse> itemDetails,
                                   PaymentResponse payment) {
}
