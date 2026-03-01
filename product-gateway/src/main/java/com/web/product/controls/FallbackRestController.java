package com.web.product.controls;

import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackRestController {

    @RequestMapping(value = "/{service}", method = {
            RequestMethod.GET,
            RequestMethod.POST,
            RequestMethod.PUT,
            RequestMethod.DELETE,
            RequestMethod.PATCH
    })
    public ResponseEntity<ProblemDetail> fallback(
            @PathVariable String service,
            ServerWebExchange exchange) {

        String originalPath = exchange.getRequest().getPath().value();

        Throwable error = exchange.getAttribute(
                ServerWebExchangeUtils.CIRCUITBREAKER_EXECUTION_EXCEPTION_ATTR
        );

        HttpStatus status = HttpStatus.SERVICE_UNAVAILABLE;
        String detailMessage = service.toUpperCase() + " is currently unavailable.";

        if (error != null) {

            if (error instanceof java.util.concurrent.TimeoutException ||
                    error.getClass().getSimpleName().contains("Timeout")) {

                status = HttpStatus.GATEWAY_TIMEOUT;
                detailMessage = service.toUpperCase() + " request timed out.";
            } else if (error.getClass().getSimpleName().contains("ConnectException")) {
                detailMessage = service.toUpperCase() + " connection failed.";
            } else if (error.getClass().getSimpleName().contains("CallNotPermittedException")) {
                detailMessage = service.toUpperCase() + " circuit breaker is open.";
            }
        }

        ProblemDetail problemDetail = ProblemDetail.forStatus(status);

        problemDetail.setTitle(status.getReasonPhrase());
        problemDetail.setDetail(detailMessage);
        problemDetail.setType(URI.create("https://api.yourdomain.com/errors/" + status.value()));
        problemDetail.setInstance(URI.create(originalPath));

        problemDetail.setProperty("service", service.toUpperCase());
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("path", originalPath);

        return ResponseEntity.status(status).body(problemDetail);
    }

    @GetMapping("/orders")
    public ResponseEntity<ProblemDetail> orderFallback(ServerHttpRequest request) {
        ProblemDetail problemDetail =
                ProblemDetail.forStatus(HttpStatus.SERVICE_UNAVAILABLE);

        problemDetail.setTitle("Order Service Unavailable");
        problemDetail.setDetail("Order Service is currently down. Please try again later.");
        problemDetail.setType(URI.create("https://api.yourdomain.com/errors/order-service-down"));
        problemDetail.setInstance(URI.create(request.getURI().getPath()));

        problemDetail.setProperty("service", "ORDER-SERVICE");
        problemDetail.setProperty("timestamp", Instant.now());

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(problemDetail);
    }

    @GetMapping("/products")
    public Map<String, String> productFallback() {
        return Map.of(
                "message", "Product service is temporarily unavailable",
                "status", "FAILED"
        );
    }

    @GetMapping("/payments")
    public Map<String, String> paymentFallback() {
        return Map.of(
                "message", "Payment service is temporarily unavailable",
                "status", "FAILED"
        );
    }

    @PostMapping("/auth")
    public Map<String, String> paymentFallback1() {
        return Map.of(
                "message", "Payment service is temporarily unavailable",
                "status", "FAILED"
        );
    }

    @GetMapping("/auth")
    public Map<String, String> paymentFallback2() {
        return Map.of(
                "message", "Payment service is temporarily unavailable",
                "status", "FAILED"
        );
    }

    @PostMapping("/auth1")
    public Mono<ProblemDetail> authFallback() {

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.SERVICE_UNAVAILABLE);

        problem.setTitle("Login Service Unavailable");
        problem.setDetail("Authentication service is temporarily unavailable. Please try again later.");
        //problem.setInstance(URI.create(exchange.getRequest().getURI().getPath()));

        // Custom properties (very useful in production)
        problem.setProperty("timestamp", Instant.now());
        problem.setProperty("service", "LOGIN-SERVICE");

        return Mono.just(problem);
    }
}
