package io.github.aniruddhamaity911.activecache.service;

import java.time.Duration;

/**
 * Abstraction for Redis cache operations.
 *
 * <p>This service hides the underlying Redis implementation from the
 * caching aspect. It provides basic cache operations required by
 * {@code @CacheRead}, {@code @CacheWrite}, and {@code @CacheEvict}.
 */
public interface RedisCacheService {

    /**
     * Retrieves a cached value.
     *
     * @param key Redis key
     * @return cached value, or {@code null} if absent
     */
    Object get(String key);

    /**
     * Stores a value in Redis.
     *
     * @param key Redis key
     * @param value value to cache
     * @param ttl expiration duration; {@code null} means no expiration
     */
    void put(String key, Object value, Duration ttl);

    /**
     * Removes a cached value.
     *
     * @param key Redis key
     */
    void evict(String key);
}
