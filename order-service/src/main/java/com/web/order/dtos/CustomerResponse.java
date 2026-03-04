package com.web.order.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public record CustomerResponse(
        Long id,
        String email,
        String firstName,
        String lastName,
        String company,
        Instant createdAt,
        String country
) {
    @JsonProperty("createdAt")
    public String formattedCreatedAt() {
        if (createdAt == null) {
            return null;
        }

        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.of("UTC"))
                .format(createdAt);
    }
}
