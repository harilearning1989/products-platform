package com.demo.utils.response;

import lombok.Builder;

@Builder
public record ApiResponse<T>(
        boolean success,
        T data,
        String message,
        long timestamp
) {}
