package com.bootcamp.pokemonservice.consumer;

import com.bootcamp.pokemonservice.dto.message.ProductViewMessage;
import com.bootcamp.pokemonservice.service.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PokemonConsumer {
    private final RedisTemplate<String, String> stringRedisTemplate;
    private final SseService sseService;

    @KafkaListener(
            id = "POKEMON_VIEW",
            topics = "POKEMON_VIEW",
            containerFactory = "productViewMessageConcurrentKafkaListenerContainerFactory"
    )
    public void processProductView(ProductViewMessage message) {
        String trendingKey = "pokemon:trending:global";

        // sorted set
        stringRedisTemplate.opsForZSet()
                .incrementScore(trendingKey, message.getPokemonName(), 1);
        sseService.sendEvent("pokemon-event", "pokemon-trending", message);
    }
}
