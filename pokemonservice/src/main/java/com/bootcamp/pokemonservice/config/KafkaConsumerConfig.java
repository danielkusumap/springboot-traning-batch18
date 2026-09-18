package com.bootcamp.pokemonservice.config;

import com.bootcamp.pokemonservice.dto.message.ProductViewMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class KafkaConsumerConfig {
    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServer;

    // buat factory buat baca pesan (Consumer)
    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> props = new HashMap<>();

        // alamat server kafka
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);

        // mengubah byte key yang diterima menjadi string
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        // mengubah byte value yang diterima menjadi Object / JSON
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        // mengizinkan spring untuk deserialisasi JSON ke class apapun
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        // tipe data default
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, Object.class.getName());

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    @Lazy
    public ConcurrentKafkaListenerContainerFactory<String, ProductViewMessage> productViewMessageConcurrentKafkaListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String, ProductViewMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        return factory;
    }
}
