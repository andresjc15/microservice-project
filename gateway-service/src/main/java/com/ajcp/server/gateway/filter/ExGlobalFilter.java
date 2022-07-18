package com.ajcp.server.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class ExGlobalFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(ExGlobalFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Ejecutando filtro pre");

        exchange.getRequest().mutate().headers(h -> h.add("token", "1561541685"));
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            log.info("Ejecutando filtro post");

            Optional.ofNullable(exchange.getRequest().getHeaders().getFirst("token")).ifPresent(value -> {
                exchange.getResponse().getHeaders().add("token", value);
            });

            exchange.getResponse().getCookies().add("color", ResponseCookie.from("color", "rojo").build());
            // exchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);
        }));
    }

    @Override
    public int getOrder() {
        // Valores -1 o menos(prioridad de orden) solo pueden ser de lectura
        // Para escritura >=0
        return 0;
    }
}
