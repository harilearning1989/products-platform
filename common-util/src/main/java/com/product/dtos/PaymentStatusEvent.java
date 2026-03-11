package com.product.dtos;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentStatusEvent(String uuid,
                                 Long orderId,
                                 String transactionId,
                                 BigDecimal amount,
                                 Instant now,
                                 String customerEmail,
                                 String failureReason) {
}
