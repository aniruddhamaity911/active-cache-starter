package io.github.aniruddhamaity911.activecache.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method that should evict a cache entry
 * after the method executes successfully.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheEvict {

    /**
     * Logical cache namespace.
     */
    String cacheName();

    /**
     * SpEL expression used to generate the cache key.
     */
    String key();
}