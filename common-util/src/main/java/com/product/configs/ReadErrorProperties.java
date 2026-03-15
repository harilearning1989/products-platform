package com.product.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "api.errors")
@Getter
@Setter
public class ReadErrorProperties {
    private String noStock;
    private String productNotFound;
}
