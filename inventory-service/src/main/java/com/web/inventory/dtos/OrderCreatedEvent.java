package com.web.inventory.dtos;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderCreatedEvent(String eventId,
                                Long orderId,
                                String customerEmail,
                                BigDecimal totalAmount,
                                List<OrderItemEvent> items,
                                Instant eventTime) {
}
