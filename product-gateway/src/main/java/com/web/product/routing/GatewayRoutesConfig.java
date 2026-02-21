package com.web.product.routing;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutesConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {

        return builder.routes()

                // LOGIN SERVICE
                .route("login-service", r -> r
                        .path("/auth/**")
                        .filters(f -> f
                                .addRequestHeader("X-Gateway", "Login-Service")
                                .removeRequestHeader("Cookie")
                                .circuitBreaker(config -> config
                                        .setName("loginServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/auth")
                                )
                        )
                        .uri("lb://LOGIN-SERVICE")
                )

                // USER SERVICE
                .route("user-service", r -> r
                        .path("/users/**")
                        .filters(f -> f
                                .addRequestHeader("X-Gateway", "User-Service")
                                .circuitBreaker(config -> config
                                        .setName("userServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/users")
                                )
                        )
                        .uri("lb://USER-SERVICE")
                )

                // PRODUCT SERVICE
                .route("product-service", r -> r
                        .path("/products/**")
                        .filters(f -> f
                                .addRequestHeader("X-Gateway", "Product-Service")
                                .circuitBreaker(config -> config
                                        .setName("productServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/products")
                                )
                        )
                        .uri("lb://PRODUCT-SERVICE")
                )

                // ORDER SERVICE
                .route("order-service", r -> r
                        .path("/orders/**")
                        .filters(f -> f
                                .addRequestHeader("X-Gateway", "Order-Service")
                                .circuitBreaker(config -> config
                                        .setName("orderServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/orders")
                                )
                        )
                        .uri("lb://ORDER-SERVICE")
                )

                // PAYMENT SERVICE
                .route("payment-service", r -> r
                        .path("/payments/**")
                        .filters(f -> f
                                .addRequestHeader("X-Gateway", "Payment-Service")
                                .circuitBreaker(config -> config
                                        .setName("paymentServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/payments")
                                )
                        )
                        .uri("lb://PAYMENT-SERVICE")
                )

                // PAYMENT HISTORY SERVICE
                .route("payment-history-service", r -> r
                        .path("/payment-history/**")
                        .filters(f -> f
                                .addRequestHeader("X-Gateway", "Payment-History-Service")
                                .circuitBreaker(config -> config
                                        .setName("paymentServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/payment-history")
                                )
                        )
                        .uri("lb://PAYMENT-HISTORY-SERVICE")
                )

                // INVENTORY SERVICE
                .route("inventory-service", r -> r
                        .path("/inventory/**")
                        .filters(f -> f
                                .addRequestHeader("X-Gateway", "Inventory-Service")
                                .circuitBreaker(config -> config
                                        .setName("inventoryServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/inventory")
                                )
                        )
                        .uri("lb://INVENTORY-SERVICE")
                )

                // NOTIFICATION SERVICE
                .route("notification-service", r -> r
                        .path("/notifications/**")
                        .filters(f -> f
                                .addRequestHeader("X-Gateway", "Notification-Service")
                                .circuitBreaker(config -> config
                                        .setName("notificationServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/notifications")
                                )
                        )
                        .uri("lb://NOTIFICATION-SERVICE")
                )

                // EMPLOYEE SERVICE
                .route("employee-service", r -> r
                        .path("/employees/**")
                        .filters(f -> f
                                .addRequestHeader("X-Gateway", "Employee-Service")
                                .circuitBreaker(config -> config
                                        .setName("employeeServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/employees")
                                )
                        )
                        .uri("lb://EMPLOYEE-SERVICE")
                )

                .build();
    }
}
