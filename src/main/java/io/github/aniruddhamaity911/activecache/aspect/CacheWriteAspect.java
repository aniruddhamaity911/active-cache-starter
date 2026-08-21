package io.github.aniruddhamaity911.activecache.aspect;


import io.github.aniruddhamaity911.activecache.annotation.CacheWrite;
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
 * Aspect responsible for handling methods annotated with {@link CacheWrite}.
 *
 * <p>The intercepted method is always executed. After successful execution,
 * the returned value is stored in Redis using the cache key and TTL defined
 * by the {@link CacheWrite} annotation.</p>
 */
@Aspect
@Component
public class CacheWriteAspect {

    private final RedisCacheService redisCacheService;
    private final RedisKeyGenerator redisKeyGenerator;
    private final SpelKeyEvaluator spelKeyEvaluator;

    /**
     * Creates a {@code CacheWriteAspect}.
     *
     * @param redisCacheService service used to store cached values
     * @param redisKeyGenerator generator used to construct the Redis cache key
     * @param spelKeyEvaluator evaluator used to resolve SpEL key expressions
     */
    public CacheWriteAspect(
            RedisCacheService redisCacheService,
            RedisKeyGenerator redisKeyGenerator,
            SpelKeyEvaluator spelKeyEvaluator
    ) {
        this.redisCacheService = redisCacheService;
        this.redisKeyGenerator = redisKeyGenerator;
        this.spelKeyEvaluator = spelKeyEvaluator;
    }

    /**
     * Intercepts methods annotated with {@link CacheWrite}.
     *
     * <p>The target method is executed first. If the method completes
     * successfully, the cache key expression is evaluated and the returned
     * value is stored in Redis.</p>
     *
     * @param joinPoint the intercepted method invocation
     * @param cacheWrite the {@link CacheWrite} annotation applied to the method
     * @return the value returned by the target method
     * @throws Throwable if execution of the intercepted method fails
     */
    @Around("@annotation(cacheWrite)")
    public Object cacheWrite(
            ProceedingJoinPoint joinPoint,
            CacheWrite cacheWrite
    ) throws Throwable {

        Object result = joinPoint.proceed();

        MethodSignature signature =
                (MethodSignature) joinPoint.getSignature();

        Method method = signature.getMethod();

        Object evaluatedKey = spelKeyEvaluator.evaluate(
                cacheWrite.key(),
                method,
                joinPoint.getArgs(),
                joinPoint.getTarget()
        );

        String redisKey = redisKeyGenerator.generate(
                cacheWrite.cacheName(),
                String.valueOf(evaluatedKey)
        );

        redisCacheService.put(
                redisKey,
                result,
                Duration.ofSeconds(cacheWrite.ttl())
        );

        return result;
    }
}
