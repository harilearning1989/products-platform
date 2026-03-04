package com.web.order.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "services")
@Getter
@Setter
public class ServiceUrlConfig {
    private String inventory;//inventory-service
    private String product;//product-service
    private String payment;
    private String customer;
}
