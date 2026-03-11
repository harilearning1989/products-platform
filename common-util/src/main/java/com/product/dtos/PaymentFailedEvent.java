package com.product.dtos;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record PaymentFailedEvent(String uuid,
                                 Long orderId,
                                 BigDecimal amount,
                                 String transactionId,
                                 Instant now,
                                 String customerEmail,
                                 String failureReason,
                                 List<OrderItemEvent> items) {
}
