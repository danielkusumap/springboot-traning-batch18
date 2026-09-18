package com.bootcamp18.gatewayservice.config;

import com.bootcamp18.gatewayservice.dto.response.BaseResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;


@Component
public class RequestHeaderFilter implements WebFilter {
    private static final String HEADER_API_KEY = "APIKey";

    @Value("${security.headers.apiKey")
    private String apiKey;

    private final ObjectMapper objectMapper;

    public RequestHeaderFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        String requestApiKey = request.getHeaders()
                .getFirst(HEADER_API_KEY);

        if (!apiKey.equals(requestApiKey)) {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            BaseResponse<Object> baseResponse = new BaseResponse<>();
            baseResponse.setMessage("Unauthorized Invalid Security Header");
            baseResponse.setData(null);

            try {
                String json = objectMapper.writeValueAsString(baseResponse);
                byte[] bytes = json.getBytes();
                DataBuffer buffer = response.bufferFactory().wrap(bytes);
                return response.writeWith(Mono.just(buffer));
            } catch (Exception e){
                String fallback = "{\"status\":\"F\",\"message\":\"" + "Unauthorized Invalid Security Header" + "\"}";
                DataBuffer buffer = response.bufferFactory().wrap(fallback.getBytes());
                return response.writeWith(Mono.just(buffer));
            }
        }
        return chain.filter(exchange);
    }
}
