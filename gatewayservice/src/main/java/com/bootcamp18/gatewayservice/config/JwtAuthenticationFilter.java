package com.bootcamp18.gatewayservice.config;


import com.bootcamp18.gatewayservice.dto.constant.ConstantVariable;
import com.bootcamp18.gatewayservice.dto.response.BaseResponse;
import com.bootcamp18.gatewayservice.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Slf4j
@Component
public class JwtAuthenticationFilter implements WebFilter {
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, ObjectMapper objectMapper) {
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        log.info(path);

        // ini tanpa SecurityConfig
//        if(isPublicEndpoint(path)){
//            return chain.filter(exchange);
//        }

        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            // ini tanpa SecurityConfig
//             return unauthorizedResponse(exchange, "Invalid Authorization header");

            // dengan SecurityConfig
            return chain.filter(exchange);
        }

        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)){
            // ini tanpa SecurityConfig
//             return unauthorizedResponse(exchange, "Invalid Authorization header");

            // dengan SecurityConfig
            return chain.filter(exchange);
        }

        String email = jwtUtil.extractEmail(token);
        Long userId = jwtUtil.extractUserId(token);

        ServerHttpRequest mutatedRequest = request.mutate()
                .header("X-User-Email", email)
                .header("X-User-Id", String.valueOf(userId))
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                email, null, Collections.emptyList()
        );

        return chain.filter(exchange.mutate().request(mutatedRequest).build())
                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
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

    private boolean isPublicEndpoint(String path){
        return ConstantVariable.PUBLIC_ENDPOINT.stream()
                .anyMatch(path::contains);
    }
}
