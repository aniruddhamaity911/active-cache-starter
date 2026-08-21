package io.github.aniruddhamaity911.activecache.key;

import org.springframework.core.env.Environment;

/**
 * Generates Redis keys for Active Cache.
 *
 * <p>The generated key follows the format:
 * {@code <spring.application.name>:<cacheName>:<key>}
 */
public class RedisKeyGenerator {
    private final Environment environment;
    public RedisKeyGenerator(Environment environment) {
        this.environment = environment;
    }

    /**
     * Generates a Redis key.
     *
     * @param cacheName logical cache namespace
     * @param key evaluated cache key
     * @return fully qualified Redis key
     */
    public String generate(String cacheName, Object key) {
        String application_name =  environment.getProperty("spring.application.name",
                "application");
        return String.format("%s:%s:%s", application_name, cacheName, key.toString());
    }
}
