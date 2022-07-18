package com.ajcp.server.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class SpringSecurityConfig {

    @Autowired
    private JwtAuthenticationFilter authenticationFilter;

    @Bean
    public SecurityWebFilterChain configure(ServerHttpSecurity http) {
        return http.authorizeExchange()
                .pathMatchers("/api/security/oauth/**").permitAll()
                .pathMatchers(HttpMethod.GET,
                        "/product-service/api/products",
                        "/user-service/users",
                        "/item-service/api/items",
                        "/item-service/api/items/{id}/quantity/{quantity}",
                        "/product-service/api/products/{id}").permitAll()
                .pathMatchers(HttpMethod.GET, "/user-service/api/users/{id}").hasAnyRole("ADMIN", "USER")
                .pathMatchers("/product-service/api/products/**",
                        "/item-service/api/items",
                        "/user-service/users").hasAnyRole("ADMIN")
                .anyExchange().authenticated()
                .and().addFilterAt(authenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .csrf().disable()
                .build();
    }

}
