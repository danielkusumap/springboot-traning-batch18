package com.bootcamp.pokemonservice.config;

import com.bootcamp.pokemonservice.dto.reponse.ResDetailPokemonDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.cache.autoconfigure.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

@Slf4j
@Configuration
@EnableCaching
public class RedisConfig {
    @Value("${spring.cache.host}")
    private String redisHost;

    @Value("${spring.cache.port}")
    private int redisPort;

    @Bean
    public JedisConnectionFactory connectionFactory(){
        RedisStandaloneConfiguration conf = new RedisStandaloneConfiguration(
                redisHost, redisPort
        );

        JedisPoolConfig poolConfig = new JedisPoolConfig();

        // mengatur jumlah koneksi yang dibuat redis
        poolConfig.setMaxTotal(100);

        // mengatur jumlah maksimal yang dibiarkan idle
        poolConfig.setMaxIdle(80);

        // mengatur jumlah minimal koneksi yang standby
        poolConfig.setMinIdle(20);

        // memastikan koneksi ditest saat diambil dari pool
        poolConfig.setTestOnBorrow(true);

        // mematikan test koneksi saat koneksi sedang idle di dalam pool
        poolConfig.setTestWhileIdle(false);

        JedisClientConfiguration jedisClientConfig =
                JedisClientConfiguration.builder()
                        // batas waktu untuk bikin koneksi baru
                        .connectTimeout(Duration.ofMillis(15000))

                        // batas waktu read
                        .readTimeout(Duration.ofMillis(15000))

                        .usePooling()
                        .poolConfig(poolConfig)
                        .build();

        return new JedisConnectionFactory(conf, jedisClientConfig);
    }

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer(
            ObjectMapper objectMapper
    ) {
        return builder -> {
            builder.cacheDefaults(
                    RedisCacheConfiguration.defaultCacheConfig()
                            .entryTtl(Duration.ofMinutes(2))
                            .computePrefixWith(
                                    cacheName -> "batch18:" + cacheName + ":"
                            )
                            .disableCachingNullValues()
            );

            // Konfigurasi khusus cache "pokemon"
            builder.withCacheConfiguration("pokemon",
                    RedisCacheConfiguration.defaultCacheConfig()
                            .entryTtl(Duration.ofMinutes(2))
                            .computePrefixWith(
                                    cacheName ->
                                            "batch18-khusus:" + cacheName + ":"
                            )
                            .serializeValuesWith(
                                    RedisSerializationContext.SerializationPair
                                            .fromSerializer(
                                                    new Jackson2JsonRedisSerializer<>(
                                                            objectMapper,
                                                            ResDetailPokemonDto.class
                                                    )
                                            )
                            )
            );
        };
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new PrefixStringRedisSerializer("batch18:"));
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer()); // Value jadi JSON
        return template;
    }

    @Bean
    public RedisTemplate<String, String> redisStringTemplate(JedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new PrefixStringRedisSerializer("batch18:"));
        template.setValueSerializer(new StringRedisSerializer()); // Value jadi JSON
        return template;
    }
}
