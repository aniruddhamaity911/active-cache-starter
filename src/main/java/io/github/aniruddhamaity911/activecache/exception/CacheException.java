package io.github.aniruddhamaity911.activecache.exception;

/**
 * Base exception for errors occurring in Active Cache.
 */
public class CacheException extends RuntimeException {

    public CacheException(String message) {
        super(message);
    }

    public CacheException(String message, Throwable cause) {
        super(message, cause);
    }
}
