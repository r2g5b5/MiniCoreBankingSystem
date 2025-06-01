package com.example.apigateway.config;

import com.example.apigateway.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                // Auth service routes - no JWT filter
                .route("auth-service", r -> r.path("/api/v1/auth/**")
                        .uri("http://localhost:9091")) // your auth-service URL/port

                // Account service routes - JWT filter applied
                .route("account-service", r -> r.path("/api/v1/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter))
                        .uri("http://localhost:9090")) // your account-service URL/port

                .build();
    }
}
