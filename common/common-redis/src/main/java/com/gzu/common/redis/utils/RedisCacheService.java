package com.gzu.common.redis.utils;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisCacheService {
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisCacheService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void set(String key, Object value, Duration ttl) {
        redisTemplate.opsForValue().set(key, value, ttl);
    }

    public Optional<Object> get(String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key));
    }

    public void setIfAbsent(String key, Object value, Duration ttl) {
        Boolean success = redisTemplate.opsForValue().setIfAbsent(key, value, ttl);
        if (!Boolean.TRUE.equals(success)) {
            throw new IllegalStateException("cache key already exists: " + key);
        }
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public boolean expire(String key, Duration ttl) {
        Boolean success = redisTemplate.expire(key, ttl.toMillis(), TimeUnit.MILLISECONDS);
        return Objects.equals(success, Boolean.TRUE);
    }
}

