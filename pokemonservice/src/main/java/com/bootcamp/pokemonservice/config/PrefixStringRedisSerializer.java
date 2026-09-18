package com.bootcamp.pokemonservice.config;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

public class PrefixStringRedisSerializer implements RedisSerializer<String> {

    private final String prefix;
    private final StringRedisSerializer delegate =
            new StringRedisSerializer();

    public PrefixStringRedisSerializer(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public byte[] serialize(String key) {
        // Tambahkan prefix sebelum key disimpan ke Redis.
        return delegate.serialize(prefix + key);
    }

    @Override
    public String deserialize(byte[] bytes) {
        // Ambil key dari Redis kemudian hapus prefix.
        String fullKey = delegate.deserialize(bytes);

        return fullKey != null
                ? fullKey.replaceFirst("^" + java.util.regex.Pattern.quote(prefix), "")
                : null;
    }
}