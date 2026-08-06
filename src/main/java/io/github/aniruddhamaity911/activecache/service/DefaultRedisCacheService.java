package io.github.aniruddhamaity911.activecache.service;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * Default implementation of {@link RedisCacheService} backed by Redis.
 */
public class DefaultRedisCacheService implements RedisCacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    public DefaultRedisCacheService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void put(String key, Object value, Duration ttl) {
        if (ttl == null || ttl.isZero() || ttl.isNegative()) {
            redisTemplate.opsForValue().set(key, value);
        } else {
            redisTemplate.opsForValue().set(key, value, ttl);
        }
    }

    @Override
    public void evict(String key) {
        redisTemplate.delete(key);
    }
}
