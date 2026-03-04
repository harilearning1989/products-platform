package com.web.payment.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.web.payment.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public record PaymentResponse(
        Long id,
        Long orderId,
        Long userId,
        String customerEmail,
        BigDecimal amount,
        PaymentStatus status,
        String transactionId,
        Instant createdAt,
        Instant updatedAt,
        String failureReason
) {
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withZone(ZoneId.of("UTC"));

    private static String formatInstant(Instant instant) {
        return instant == null ? null : FORMATTER.format(instant);
    }

    @JsonProperty("createdAt")
    public String formattedCreatedAt() {
        return formatInstant(createdAt);
    }

    @JsonProperty("updatedAt")
    public String formattedUpdatedAt() {
        return formatInstant(updatedAt);
    }
}
