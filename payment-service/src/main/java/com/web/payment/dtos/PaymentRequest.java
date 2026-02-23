package com.web.payment.dtos;

import java.math.BigDecimal;

public record PaymentRequest(
        Long orderId,
        String customerEmail,
        BigDecimal amount
) {}
