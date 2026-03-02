package com.web.product.routing;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class GatewayRoutesConfig {

    /*
    IMPORTANT: Retry Only Works For

    ✔ 5xx status
    ✔ Network errors
    ✔ Timeouts

    It will NOT retry:

    ❌ 400
    ❌ 404
    ❌ 401

    Unless you explicitly configure it.

    ConnectException
    UnknownHostException
    ReadTimeoutException
     */

    /*
    Dynamic Routing (Path-Based Routing)
    Load Balancing Works with Eureka or other service discovery tools.
    Authentication & Authorization (JWT/OAuth2)
    Rate Limiting Limit the number of requests per user/IP.
    Circuit Breaker (Fault Tolerance)
    Request & Response Modification
    URL Rewriting
    CORS Configuration  Handle cross-origin requests globally.
    Logging & Monitoring
    API Versioning
     */

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {

        return builder.routes()

                // LOGIN SERVICE
                .route("login-service", r -> r
                        .path("/auth/**")
                        .filters(f -> f
                                .addRequestHeader("Authorization", "Bearer token")
                                .addRequestHeader("X-Gateway", "Login-Service")
                                .addRequestHeader("X-Gateway-Name", "Hari")
                                //.removeRequestHeader("Cookie")
                                .retry(retry -> retry
                                        .setRetries(2)
                                        .setStatuses(HttpStatus.INTERNAL_SERVER_ERROR)
                                )
                                .circuitBreaker(config -> config
                                        .setName("loginServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/login-service")
                                )
                        )
                        .uri("lb://LOGIN-SERVICE")
                )

                // USER SERVICE
                .route("customer-service", r -> r
                        .path("/customers/**")
                        .filters(f -> f
                                .addRequestHeader("X-Gateway", "Customer-Service")
                                .addRequestHeader("Authorization", "Bearer token")
                                .addRequestHeader("X-Gateway-Name", "Hari")
                                //.removeRequestHeader("Cookie")
                                .retry(retry -> retry
                                        .setRetries(2)
                                        .setStatuses(HttpStatus.INTERNAL_SERVER_ERROR)
                                )
                                .circuitBreaker(config -> config
                                        .setName("customerServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/customer-service")
                                )
                        )
                        .uri("lb://CUSTOMER-SERVICE")
                )

                // PRODUCT SERVICE
                .route("product-service", r -> r
                        .path("/products/**")
                        .filters(f -> f
                                .addRequestHeader("Authorization", "Bearer token")
                                .addRequestHeader("X-Gateway-Name", "Hari")
                                //.removeRequestHeader("Cookie")
                                .retry(retry -> retry
                                        .setRetries(2)
                                        .setStatuses(HttpStatus.INTERNAL_SERVER_ERROR)
                                )
                                .circuitBreaker(config -> config
                                        .setName("productServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/product-service")
                                )
                        )
                        .uri("lb://PRODUCT-SERVICE")
                )

                // ORDER SERVICE
                .route("order-service", r -> r
                        .path("/orders/**")
                        .filters(f -> f
                                .addRequestHeader("Authorization", "Bearer token")
                                .addRequestHeader("X-Gateway-Name", "Hari")
                                //.removeRequestHeader("Cookie")
                                .retry(retry -> retry
                                        .setRetries(2)
                                        .setStatuses(HttpStatus.INTERNAL_SERVER_ERROR)
                                )
                                .circuitBreaker(config -> config
                                        .setName("orderServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/order-service")
                                )
                        )
                        .uri("lb://ORDER-SERVICE")
                )

                // PAYMENT SERVICE
                .route("payment-service", r -> r
                        .path("/payments/**")
                        .filters(f -> f
                                .addRequestHeader("Authorization", "Bearer token")
                                .addRequestHeader("X-Gateway-Name", "Hari")
                                //.removeRequestHeader("Cookie")
                                .retry(retry -> retry
                                        .setRetries(2)
                                        .setStatuses(HttpStatus.INTERNAL_SERVER_ERROR)
                                )
                                .circuitBreaker(config -> config
                                        .setName("paymentServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/payment-service")
                                )
                        )
                        .uri("lb://PAYMENT-SERVICE")
                )

                // PAYMENT HISTORY SERVICE
                .route("payment-history-service", r -> r
                        .path("/payment-history/**")
                        .filters(f -> f
                                .addRequestHeader("Authorization", "Bearer token")
                                .addRequestHeader("X-Gateway-Name", "Hari")
                                //.removeRequestHeader("Cookie")
                                .retry(retry -> retry
                                        .setRetries(2)
                                        .setStatuses(HttpStatus.INTERNAL_SERVER_ERROR)
                                )
                                .circuitBreaker(config -> config
                                        .setName("paymentHistoryServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/payment-history-service")
                                )
                        )
                        .uri("lb://PAYMENT-HISTORY-SERVICE")
                )

                // INVENTORY SERVICE
                .route("inventory-service", r -> r
                        .path("/inventory/**")
                        .filters(f -> f
                                .addRequestHeader("Authorization", "Bearer token")
                                .addRequestHeader("X-Gateway-Name", "Hari")
                                //.removeRequestHeader("Cookie")
                                .retry(retry -> retry
                                        .setRetries(2)
                                        .setStatuses(HttpStatus.INTERNAL_SERVER_ERROR)
                                )
                                .circuitBreaker(config -> config
                                        .setName("inventoryServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/inventory-service")
                                )
                        )
                        .uri("lb://INVENTORY-SERVICE")
                )

                // NOTIFICATION SERVICE
                .route("notification-service", r -> r
                        .path("/notifications/**")
                        .filters(f -> f
                                .addRequestHeader("Authorization", "Bearer token")
                                .addRequestHeader("X-Gateway-Name", "Hari")
                                //.removeRequestHeader("Cookie")
                                .retry(retry -> retry
                                        .setRetries(2)
                                        .setStatuses(HttpStatus.INTERNAL_SERVER_ERROR)
                                )
                                .circuitBreaker(config -> config
                                        .setName("notificationServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/notification-service")
                                )
                        )
                        .uri("lb://NOTIFICATION-SERVICE")
                )

                // EMPLOYEE SERVICE
                .route("employee-service", r -> r
                        .path("/employees/**")
                        .filters(f -> f
                                .addRequestHeader("Authorization", "Bearer token")
                                .addRequestHeader("X-Gateway-Name", "Hari")
                                //.removeRequestHeader("Cookie")
                                .retry(retry -> retry
                                        .setRetries(2)
                                        .setStatuses(HttpStatus.INTERNAL_SERVER_ERROR)
                                )
                                .circuitBreaker(config -> config
                                        .setName("employeeServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/employee-service")
                                )
                        )
                        .uri("lb://EMPLOYEE-SERVICE")
                )

                .build();
    }

}
