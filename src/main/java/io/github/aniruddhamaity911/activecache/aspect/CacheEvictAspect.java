package io.github.aniruddhamaity911.activecache.aspect;


import io.github.aniruddhamaity911.activecache.annotation.CacheEvict;
import io.github.aniruddhamaity911.activecache.key.RedisKeyGenerator;
import io.github.aniruddhamaity911.activecache.key.SpelKeyEvaluator;
import io.github.aniruddhamaity911.activecache.service.RedisCacheService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Aspect responsible for handling methods annotated with {@link CacheEvict}.
 *
 * <p>The intercepted method is executed first. If the method completes
 * successfully, the corresponding cache entry is removed from Redis.</p>
 */
@Aspect
public class CacheEvictAspect {
    private static final Logger LOG = LoggerFactory.getLogger(CacheEvictAspect.class);
    private final RedisCacheService redisCacheService;
    private final RedisKeyGenerator redisKeyGenerator;
    private final SpelKeyEvaluator spelKeyEvaluator;

    /**
     * Creates a {@code CacheEvictAspect}.
     *
     * @param redisCacheService service used to remove cached values
     * @param redisKeyGenerator generator used to construct the Redis cache key
     * @param spelKeyEvaluator evaluator used to resolve SpEL key expressions
     */
    public CacheEvictAspect(
            RedisCacheService redisCacheService,
            RedisKeyGenerator redisKeyGenerator,
            SpelKeyEvaluator spelKeyEvaluator
    ) {
        LOG.info("Configuring CacheEvictAspect...");
        this.redisCacheService = redisCacheService;
        this.redisKeyGenerator = redisKeyGenerator;
        this.spelKeyEvaluator = spelKeyEvaluator;
        LOG.info("CacheEvictAspect configured");
    }

    /**
     * Intercepts methods annotated with {@link CacheEvict}.
     *
     * <p>The target method is executed before the cache entry is removed.
     * This ensures that the cache is evicted only when the method completes
     * successfully. If the method throws an exception, the existing cache
     * entry remains unchanged.</p>
     *
     * @param joinPoint the intercepted method invocation
     * @param cacheEvict the {@link CacheEvict} annotation applied to the method
     * @return the value returned by the target method
     * @throws Throwable if execution of the intercepted method fails
     */
    @Around("@annotation(cacheEvict)")
    public Object cacheEvict(
            ProceedingJoinPoint joinPoint,
            CacheEvict cacheEvict
    ) throws Throwable {
        LOG.info("CacheEvictAspect starting...");

        Object result = joinPoint.proceed();

        MethodSignature signature =
                (MethodSignature) joinPoint.getSignature();

        Method method = signature.getMethod();

        Object evaluatedKey = spelKeyEvaluator.evaluate(
                cacheEvict.key(),
                method,
                joinPoint.getArgs(),
                joinPoint.getTarget()
        );

        String redisKey = redisKeyGenerator.generate(
                cacheEvict.cacheName(),
                String.valueOf(evaluatedKey)
        );
        LOG.debug("Evicting cache using key {}",redisKey);
        redisCacheService.evict(redisKey);
        LOG.debug("Evicted cache using key {}",redisKey);
        LOG.info("CacheEvictAspect ending...");
        return result;
    }
}