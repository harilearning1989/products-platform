package com.product.dtos;

public record PaymentStatusEvent(String uuid,
                                 Long orderId,
                                 String transactionId,
                                 String failureReason) {
}
