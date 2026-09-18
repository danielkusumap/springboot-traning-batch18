package com.bootcamp18.gatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // menandakan class ini berisi configurasi Bean untuk spring
public class GatewayConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder){
        return builder.routes()
                .route("userservice", r -> r
                        .path("/gateway/user/**")
                        .filters(f -> {
                            f.rewritePath(
                                    "/gateway/user/(?<segment>.*)",
                                    "/user/${segment}"
                            );
                            return f;
                        })
                        .uri("lb://userservice"))
                .route("pokemonservice", r -> r
                        .path("/gateway/pokemon/**")
                        .filters(f -> {
                            f.rewritePath(
                                    "/gateway/pokemon/(?<segment>.*)",
                                    "/pokemon/${segment}"
                            );
                            return f;
                        })
                        .uri("lb://pokemonservice")
                )
                .build();
    }
}
