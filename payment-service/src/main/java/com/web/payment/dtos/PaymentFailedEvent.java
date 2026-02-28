package com.web.payment.dtos;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record PaymentFailedEvent(String uuid,
                                 Long orderId,
                                 BigDecimal amount,
                                 String transactionId,
                                 Instant now,
                                 String failureReason,
                                 List<OrderItemEvent> items) {
}
