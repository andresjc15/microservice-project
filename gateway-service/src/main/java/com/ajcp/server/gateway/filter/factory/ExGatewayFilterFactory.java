package com.ajcp.server.gateway.filter.factory;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class ExGatewayFilterFactory extends AbstractGatewayFilterFactory<ExGatewayFilterFactory.FilterConfiguration> {

    private static final Logger log = LoggerFactory.getLogger(ExGatewayFilterFactory.class);

    public ExGatewayFilterFactory() {
        super(FilterConfiguration.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("message", "cookieName", "cookieValue");
    }

    @Override
    public GatewayFilter apply(FilterConfiguration config) {
        return new OrderedGatewayFilter((exchange, chain) -> {
            log.info("[Ejecutando pre gateway filter factory] " + config.message);
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {

                Optional.ofNullable(config.cookieValue).ifPresent(cookie -> {
                    exchange.getResponse().addCookie(ResponseCookie.from(config.cookieName, cookie).build());
                });

                log.info("[Ejecutando post gateway filter factory] " + config.message);
            }));
        }, 2);
    }

    @Data
    public static class FilterConfiguration {

        private String message;
        private String cookieValue;
        private String cookieName;

    }

}