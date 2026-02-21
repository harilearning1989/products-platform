package com.web.product.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gateway.security")
public record SecurityProperties(
        boolean login,
        boolean user,
        boolean product,
        boolean order,
        boolean payment,
        boolean paymentHistory,
        boolean inventory,
        boolean notification,
        boolean employee

) {}
