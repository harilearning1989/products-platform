package com.product.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
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
    public static PaymentResponse empty(Long orderId) {
        return new PaymentResponse(
                null,
                orderId,
                null,
                null,
                BigDecimal.ZERO,
                "NOT_PAID",
                null,
                null,
                null,
                null
        );
    }
}
