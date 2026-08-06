package io.github.aniruddhamaity911.activecache.annotation;

import java.lang.annotation.*;

/**
 * Marks a method whose result should be retrieved from the cache
 * before executing the method. If no cached value exists, the method
 * is executed and its result is cached.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface CacheRead {
    /**
     * Logical cache name.
     */
    String cacheName() default "";
    /**
     * SpEL expression used to generate the cache key.
     */
    String key();
    /**
     * Time-to-live in seconds.
     * A value <= 0 means no expiration.
     */
    long ttl() default 60;
}
