package com.bootcamp.pokemonservice.aspect;

import com.bootcamp.pokemonservice.dto.message.ProductViewMessage;
import com.bootcamp.pokemonservice.dto.reponse.ResDetailPokemonDto;
import com.bootcamp.pokemonservice.producer.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class PokemonTrackingAspect {
    private final KafkaProducer<ProductViewMessage> kafkaProducer;

    @AfterReturning(
            pointcut = "execution(* com.bootcamp.pokemonservice.service.impl.PokemonServiceImpl.getPokemonById(..))",
            returning = "result"
    )
    public void trackPokemon(JoinPoint joinPoint, Object result){
        if (result instanceof ResDetailPokemonDto pokemonDto) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            ProductViewMessage message = new ProductViewMessage(
                    pokemonDto.getName(),
                    LocalDateTime.now().format(formatter)
            );

            kafkaProducer.sendMessage("POKEMON_VIEW", message);
        }
    }
}
