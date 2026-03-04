package com.web.demo.records;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

public record Customer(
        Long id,
        String email,
        @JsonAlias("first")
        String firstName,
        @JsonAlias("last")
        String lastName,
        String company,
        @JsonAlias("created_at")
        Instant createdAt,
        String country
) {}
