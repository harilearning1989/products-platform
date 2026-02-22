package com.web.notification.dtos;

import java.math.BigDecimal;

public record OrderCreatedEvent(
        String eventId,
        Long orderId,
        String customerEmail,
        BigDecimal amount
) {}
