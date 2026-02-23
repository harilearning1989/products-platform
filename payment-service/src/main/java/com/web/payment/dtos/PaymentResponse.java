package com.web.payment.dtos;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentResponse(
        Long id,
        Long orderId,
        BigDecimal amount,
        String status,
        String transactionId,
        Instant createdAt
) {}
