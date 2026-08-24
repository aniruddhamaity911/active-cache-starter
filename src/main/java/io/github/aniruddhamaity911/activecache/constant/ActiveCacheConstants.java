package io.github.aniruddhamaity911.activecache.constant;

/**
 * Constants used by Active Cache.
 */
public final class ActiveCacheConstants {

    /**
     * Separator used between parts of a Redis cache key.
     */
    public static final String KEY_SEPARATOR = ":";

    /**
     * Default application name used when spring.application.name is not configured.
     */
    public static final String DEFAULT_APPLICATION_NAME = "application";

    private ActiveCacheConstants() {
        // Utility class
    }
}
