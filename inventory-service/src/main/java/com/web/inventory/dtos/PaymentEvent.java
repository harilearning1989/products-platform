package com.web.inventory.dtos;

import java.math.BigDecimal;

public record PaymentEvent(String uuid,Long orderId, Integer userId, Long productId, Integer quantity, BigDecimal amount) {
}
