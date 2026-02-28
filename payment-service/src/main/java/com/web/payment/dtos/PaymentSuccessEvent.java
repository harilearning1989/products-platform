package com.web.payment.dtos;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentSuccessEvent(String uuid,
                                  Long orderId,
                                  BigDecimal amount,
                                  String transactionId,
                                  Instant now,
                                  String failureReason) {
}
