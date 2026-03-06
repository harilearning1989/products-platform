package com.product.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.product.util.DateUtils;

import java.time.Instant;

public record CustomerResponse(
        Long userId,
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

        return DateUtils.formatUtc(createdAt);
    }
}
