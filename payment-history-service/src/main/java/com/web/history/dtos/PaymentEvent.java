package com.web.history.dtos;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentEvent(
        String eventId,
        Long paymentId,
        Long orderId,
        String customerEmail,
        BigDecimal amount,
        String status,
        String transactionId,
        Instant eventTime
) {}
