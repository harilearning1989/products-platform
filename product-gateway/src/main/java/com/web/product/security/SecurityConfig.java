package com.web.product.security;

import com.web.product.config.SecurityProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("${gateway.security.enabled}")
    private boolean securityEnabled;

    private final SecurityProperties securityProperties;
    private final JwtAuthenticationManager authenticationManager;
    private final JwtServerAuthenticationConverter converter;

    public SecurityConfig(SecurityProperties securityProperties,
                          JwtAuthenticationManager authenticationManager,
                          JwtServerAuthenticationConverter converter) {
        this.authenticationManager = authenticationManager;
        this.converter = converter;
        this.securityProperties = securityProperties;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        AuthenticationWebFilter authFilter =
                new AuthenticationWebFilter(authenticationManager);
        authFilter.setServerAuthenticationConverter(converter);
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> {

                    // LOGIN
                    if (!securityProperties.login()) {
                        exchanges.pathMatchers("/auth/**").permitAll();
                    } else {
                        exchanges.pathMatchers("/auth/**").authenticated();
                    }

                    // USER
                    if (!securityProperties.user()) {
                        exchanges.pathMatchers("/users/**").permitAll();
                    } else {
                        exchanges.pathMatchers("/users/**")
                                .hasAnyRole("USER", "ADMIN");
                    }

                    // PRODUCT
                    if (!securityProperties.product()) {
                        exchanges.pathMatchers("/products/**").permitAll();
                    } else {
                        exchanges.pathMatchers("/products/**")
                                .hasAnyRole("USER", "ADMIN");
                    }

                    // ORDER
                    if (!securityProperties.order()) {
                        exchanges.pathMatchers("/orders/**").permitAll();
                    } else {
                        exchanges.pathMatchers("/orders/**")
                                .hasRole("USER");
                    }

                    // PAYMENT
                    if (!securityProperties.payment()) {
                        exchanges.pathMatchers("/payments/**").permitAll();
                    } else {
                        exchanges.pathMatchers("/payments/**")
                                .hasRole("USER");
                    }

                    // PAYMENT HISTORY
                    if (!securityProperties.paymentHistory()) {
                        exchanges.pathMatchers("/payment-history/**").permitAll();
                    } else {
                        exchanges.pathMatchers("/payment-history/**")
                                .hasRole("USER");
                    }

                    // INVENTORY
                    if (!securityProperties.inventory()) {
                        exchanges.pathMatchers("/inventory/**").permitAll();
                    } else {
                        exchanges.pathMatchers("/inventory/**")
                                .hasRole("ADMIN");
                    }

                    // NOTIFICATION
                    if (!securityProperties.notification()) {
                        exchanges.pathMatchers("/notifications/**").permitAll();
                    } else {
                        exchanges.pathMatchers("/notifications/**")
                                .hasRole("ADMIN");
                    }

                    // EMPLOYEE
                    if (!securityProperties.employee()) {
                        exchanges.pathMatchers("/employees/**").permitAll();
                    } else {
                        exchanges.pathMatchers("/employees/**")
                                .hasRole("ADMIN");
                    }

                    exchanges.anyExchange().authenticated();
                })
                .addFilterAt(authFilter, SecurityWebFiltersOrder.AUTHENTICATION);

        return http.build();
    }

   /* @Bean
    public Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter() {

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<String> roles = jwt.getClaimAsStringList("roles");
            if (roles == null) return List.of();
            return roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList();
        });

        return new ReactiveJwtAuthenticationConverterAdapter(converter);
    }*/
/*
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http.csrf(ServerHttpSecurity.CsrfSpec::disable);
        if (!securityEnabled) {
            // 🔥 Authentication disabled
            http.authorizeExchange(exchanges -> exchanges
                    .anyExchange().permitAll()
            );
            return http.build();
        }
        // 🔥 Authentication enabled
        http.authorizeExchange(exchanges -> exchanges
                        // Public
                        .pathMatchers("/auth/**").permitAll()
                        // Admin
                        .pathMatchers("/admin/**").hasRole("ADMIN")
                        // Product
                        .pathMatchers("/products/**").hasAnyRole("USER", "ADMIN")
                        // Payments
                        .pathMatchers("/payments/**").hasRole("USER")
                        // Employees
                        .pathMatchers("/employees/**").hasRole("ADMIN")
                        .anyExchange().authenticated()
                );
        return http.build();
    }
 */

}
