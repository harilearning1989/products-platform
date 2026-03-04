package com.web.order.dtos;

import java.math.BigDecimal;

public record PaymentResponse(Long id,
                              Long orderId,
                              Long userId,
                              String customerEmail,
                              BigDecimal amount,
                              String status,
                              String transactionId,
                              String createdAt,
                              String updatedAt,
                              String failureReason) {
}
