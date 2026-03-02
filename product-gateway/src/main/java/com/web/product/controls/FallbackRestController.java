package com.web.product.controls;

import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;
import java.time.Instant;

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

}
