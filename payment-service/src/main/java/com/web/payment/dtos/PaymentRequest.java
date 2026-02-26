package com.web.payment.dtos;

import java.math.BigDecimal;

public record PaymentRequest(
        Long orderId, Integer userId, Long productId, Integer quantity, BigDecimal amount
) {}
