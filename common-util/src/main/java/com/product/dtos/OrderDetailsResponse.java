package com.product.dtos;

import com.product.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

public record OrderDetailsResponse(Long orderId,
                                   Long userId,
                                   OrderStatus status,
                                   BigDecimal totalAmount,
                                   CustomerResponse customerResponse,
                                   List<OrderDetailItemResponse> itemDetails,
                                   PaymentResponse payment) {
}
