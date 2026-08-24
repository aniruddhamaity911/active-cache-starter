package io.github.aniruddhamaity911.activecache.key;

import io.github.aniruddhamaity911.activecache.constant.ActiveCacheConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

/**
 * Generates Redis keys for Active Cache.
 *
 * <p>The generated key follows the format:
 * {@code <spring.application.name>:<cacheName>:<key>}
 */
public class RedisKeyGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(RedisKeyGenerator.class);
    private final Environment environment;
    public RedisKeyGenerator(Environment environment) {
        LOG.info("Initiating RedisKeyGenerator");
        this.environment = environment;
        LOG.info("RedisKeyGenerator initialized");
    }

    /**
     * Generates a Redis key.
     *
     * @param cacheName logical cache namespace
     * @param key evaluated cache key
     * @return fully qualified Redis key
     */
    public String generate(String cacheName, Object key) {
        LOG.debug("Generating key {} for value {}",cacheName,key);
        String application_name =  environment.getProperty("spring.application.name",
                ActiveCacheConstants.DEFAULT_APPLICATION_NAME);
        return String.format("%s%s%s%s%s", application_name,ActiveCacheConstants.KEY_SEPARATOR ,cacheName,ActiveCacheConstants.KEY_SEPARATOR ,key.toString());
    }
}
