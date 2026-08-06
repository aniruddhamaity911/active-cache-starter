package io.github.aniruddhamaity911.activecache.annotation;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface CacheWrite {

    /**
     * Logical cache namespace.
     */
    String cacheName();

    /**
     * SpEL expression used to generate the cache key.
     */
    String key();

    /**
     * Time-to-live in seconds.
     */
    long ttl() default 60;
}
