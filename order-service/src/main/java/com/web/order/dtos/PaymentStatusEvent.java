package com.web.order.dtos;

public record PaymentStatusEvent(String uuid,
                                 Long orderId,
                                 String transactionId,
                                 String failureReason) {
}
