package com.web.notification.dtos;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentFailedEvent(String uuid,
                                 Long orderId,
                                 BigDecimal amount,
                                 String transactionId,
                                 Instant now,
                                 String failureReason,
                                 String customerEmail) {
}
