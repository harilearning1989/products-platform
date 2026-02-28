package com.web.payment.dtos;

import com.web.payment.models.Payment;

import java.util.List;

public record PaymentProcessedInternalEvent(
        Payment payment,
        List<OrderItemEvent> items
) {}
