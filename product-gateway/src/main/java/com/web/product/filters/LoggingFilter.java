package com.web.product.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String requestId = UUID.randomUUID().toString();
        long startTime = System.currentTimeMillis();

        exchange.getRequest()
                .mutate()
                .header("X-Request-Id", requestId)
                .build();

        String path = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethod().name();

        return chain.filter(exchange)
                .doOnSuccess(aVoid -> {
                    long duration = System.currentTimeMillis() - startTime;
                    int status = exchange.getResponse().getStatusCode().value();

                    log.info("RequestId={} Method={} Path={} Status={} Duration={}ms",
                            requestId, method, path, status, duration);
                });
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
