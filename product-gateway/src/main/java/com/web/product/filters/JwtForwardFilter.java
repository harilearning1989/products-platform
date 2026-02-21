package com.web.product.filters;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtForwardFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        return exchange.getPrincipal()
                .cast(Authentication.class)
                .flatMap(authentication -> {

                    String username = authentication.getName();

                    var mutatedRequest = exchange.getRequest()
                            .mutate()
                            .header("X-User-Name", username)
                            .build();

                    return chain.filter(
                            exchange.mutate()
                                    .request(mutatedRequest)
                                    .build()
                    );
                })
                .switchIfEmpty(chain.filter(exchange));
    }
}
