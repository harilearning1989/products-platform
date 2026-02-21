package com.web.product.controls;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Instant;
import java.util.Map;

@RestController
public class FallbackRestController {

    @GetMapping("/fallback/products")
    public Map<String, String> productFallback() {
        return Map.of(
                "message", "Product service is temporarily unavailable",
                "status", "FAILED"
        );
    }

    @GetMapping("/fallback/payments")
    public Map<String, String> paymentFallback() {
        return Map.of(
                "message", "Payment service is temporarily unavailable",
                "status", "FAILED"
        );
    }

    @PostMapping("/fallback/auth")
    public Mono<ProblemDetail> authFallback(ServerWebExchange exchange) {

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.SERVICE_UNAVAILABLE);

        problem.setTitle("Login Service Unavailable");
        problem.setDetail("Authentication service is temporarily unavailable. Please try again later.");
        problem.setInstance(URI.create(exchange.getRequest().getURI().getPath()));

        // Custom properties (very useful in production)
        problem.setProperty("timestamp", Instant.now());
        problem.setProperty("service", "LOGIN-SERVICE");

        return Mono.just(problem);
    }
}
