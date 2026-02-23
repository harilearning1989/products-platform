package com.web.order.dtos;

public record OrderCreatedEvent(String string, Long orderId, String email, String amount) {
}
