package io.github.aniruddhamaity911.activecache.exception;

/**
 * Thrown when a cache value cannot be serialized or deserialized.
 */
public class CacheSerializationException extends RuntimeException {

    public CacheSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}