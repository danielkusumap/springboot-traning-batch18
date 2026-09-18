package com.bootcamp18.gatewayservice.config;

import com.bootcamp18.gatewayservice.dto.response.BaseResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final RequestHeaderFilter requestHeaderFilter;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable) // matikan pengecekan csrf
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)  // matikan form login default
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .logout(ServerHttpSecurity.LogoutSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        // public endpoint
                        .pathMatchers(
                                "/gateway/user/rest/v1/auth/login",
                                "/gateway/user/rest/v1/user/register"
                        )
                        .permitAll()

                        // kalo mau set endpoint yang butuh token
                        .pathMatchers(
                                "/gateway/user/rest/v1/user/**",
                                "/gateway/pokemon/rest/v1/**"
                        ).authenticated()

                        .anyExchange().authenticated()  // url apapun wajib punya token
                )

                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((exchange, ex) -> {
                            return unauthorizedResponse(exchange, "Unauthorized or Missing Authorization");
                        })
                )

                .addFilterBefore(
                        requestHeaderFilter,
                        SecurityWebFiltersOrder.AUTHENTICATION
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        SecurityWebFiltersOrder.AUTHENTICATION
                )

                .build();
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String message){
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        try{
            BaseResponse<Object> baseResponse = new BaseResponse<>();
            baseResponse.setMessage(message);
            baseResponse.setData(null);
            String json = objectMapper.writeValueAsString(baseResponse);
            DataBuffer buffer = response.bufferFactory().wrap(json.getBytes());
            return response.writeWith(Mono.just(buffer));
        } catch (Exception e){
            String fallback = "{\"status\":\"F\",\"message\":\"" + message + "\"}";
            DataBuffer buffer = response.bufferFactory().wrap(fallback.getBytes());
            return response.writeWith(Mono.just(buffer));
        }
    }
}
