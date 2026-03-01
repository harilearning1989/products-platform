package com.web.history.dtos;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentSuccessEvent(String uuid,
                                  Long orderId,
                                  BigDecimal amount,
                                  String transactionId,
                                  Instant now,
                                  String customerEmail,
                                  String failureReason) {
}
