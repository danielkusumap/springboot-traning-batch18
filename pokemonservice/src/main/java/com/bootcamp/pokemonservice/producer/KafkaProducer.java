package com.bootcamp.pokemonservice.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducer<T> {
    private final KafkaTemplate<String, T> kafkaTemplate;

    public void sendMessage(String topic, T message){
        log.info("Sending topic {}");
        kafkaTemplate.send(topic, message)
                .whenComplete((result, ex) -> {
                    if (ex != null){
                        log.error("Kafka send failed", ex);
                    } else {
                        log.info(
                                "Kafka send success. topic {}",
                                result.getRecordMetadata().topic()
                        );
                    }
                });
    }
}
