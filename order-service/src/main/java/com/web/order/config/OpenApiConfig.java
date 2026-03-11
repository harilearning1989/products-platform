package com.web.order.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Order Service API",
                version = "1.0",
                description = "REST APIs for managing orders in the e-commerce platform",
                contact = @Contact(
                        name = "API Support Team",
                        email = "support@orderservice.com",
                        url = "https://orderservice.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"
                )
        ),
        servers = {
                @Server(
                        description = "Local Environment",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "Development Server",
                        url = "https://dev.orderservice.com"
                )
        },
        security = {
                @SecurityRequirement(name = "bearerAuth")
        }
)

@SecurityScheme(
        name = "bearerAuth",
        description = "JWT Authorization header using Bearer scheme",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT"
)

public class OpenApiConfig {
}
