package io.github.aniruddhamaity911.activecache.aspect;


import io.github.aniruddhamaity911.activecache.annotation.CacheRead;
import io.github.aniruddhamaity911.activecache.key.RedisKeyGenerator;
import io.github.aniruddhamaity911.activecache.key.SpelKeyEvaluator;
import io.github.aniruddhamaity911.activecache.service.RedisCacheService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;

/**
 * Aspect responsible for handling methods annotated with {@link CacheRead}.
 *
 * <p>The aspect checks whether a value is already available in Redis before
 * executing the target method. If a cached value exists, it is returned
 * directly. Otherwise, the target method is executed and its result is
 * stored in Redis before being returned.</p>
 */
@Aspect
@Component
public class CacheReadAspect {

    private final RedisCacheService redisCacheService;
    private final RedisKeyGenerator redisKeyGenerator;
    private final SpelKeyEvaluator spelKeyEvaluator;

    /**
     * Creates a {@code CacheReadAspect}.
     *
     * @param redisCacheService service used to retrieve and store cached values
     * @param redisKeyGenerator generator used to construct the Redis cache key
     * @param spelKeyEvaluator evaluator used to resolve SpEL key expressions
     */
    public CacheReadAspect(
            RedisCacheService redisCacheService,
            RedisKeyGenerator redisKeyGenerator,
            SpelKeyEvaluator spelKeyEvaluator
    ) {
        this.redisCacheService = redisCacheService;
        this.redisKeyGenerator = redisKeyGenerator;
        this.spelKeyEvaluator = spelKeyEvaluator;
    }

    /**
     * Intercepts methods annotated with {@link CacheRead} and applies
     * cache-read behavior.
     *
     * <p>The cache key expression defined in {@link CacheRead#key()} is first
     * evaluated using the method arguments. The resulting value is combined
     * with the application name and cache name to create the final Redis key.</p>
     *
     * <p>If a value is found in Redis, the target method is not executed.
     * If no value is found, the method is executed and its result is stored
     * in Redis using the configured TTL.</p>
     *
     * @param joinPoint the intercepted method invocation
     * @param cacheRead the {@link CacheRead} annotation applied to the method
     * @return the cached value or the result returned by the target method
     * @throws Throwable if execution of the intercepted method fails
     */
    @Around("@annotation(cacheRead)")
    public Object cacheRead(
            ProceedingJoinPoint joinPoint,
            CacheRead cacheRead
    ) throws Throwable {

        MethodSignature signature =
                (MethodSignature) joinPoint.getSignature();

        Method method = signature.getMethod();

        Object evaluatedKey = spelKeyEvaluator.evaluate(
                cacheRead.key(),
                method,
                joinPoint.getArgs(),
                joinPoint.getTarget()
        );

        String redisKey = redisKeyGenerator.generate(
                cacheRead.cacheName(),
                String.valueOf(evaluatedKey)
        );

        Object cachedValue = redisCacheService.get(redisKey);

        if (cachedValue != null) {
            return cachedValue;
        }

        Object result = joinPoint.proceed();

        redisCacheService.put(
                redisKey,
                result,
                Duration.ofSeconds(cacheRead.ttl())
        );

        return result;
    }
}