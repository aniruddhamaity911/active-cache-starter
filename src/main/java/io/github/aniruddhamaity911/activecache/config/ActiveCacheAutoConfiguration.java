package io.github.aniruddhamaity911.activecache.config;


import org.springframework.beans.factory.annotation.Qualifier;
import tools.jackson.databind.ObjectMapper;
import io.github.aniruddhamaity911.activecache.aspect.CacheEvictAspect;
import io.github.aniruddhamaity911.activecache.aspect.CacheReadAspect;
import io.github.aniruddhamaity911.activecache.aspect.CacheWriteAspect;
import io.github.aniruddhamaity911.activecache.key.RedisKeyGenerator;
import io.github.aniruddhamaity911.activecache.service.DefaultRedisCacheService;
import io.github.aniruddhamaity911.activecache.service.RedisCacheService;
import io.github.aniruddhamaity911.activecache.key.SpelKeyEvaluator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Autoconfiguration for Active Cache.
 *
 * <p>Registers the infrastructure required for annotation-based
 * Redis caching.
 */
@Configuration
public class ActiveCacheAutoConfiguration {

    @Bean("activeCacheRedisTemplate")
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory connectionFactory,
            ObjectMapper objectMapper) {

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(connectionFactory);

        StringRedisSerializer keySerializer = new StringRedisSerializer();

        GenericJacksonJsonRedisSerializer valueSerializer =
                new GenericJacksonJsonRedisSerializer(objectMapper);

        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setHashKeySerializer(keySerializer);

        redisTemplate.setValueSerializer(valueSerializer);
        redisTemplate.setHashValueSerializer(valueSerializer);

        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    @Bean
    public RedisCacheService redisCacheService(
            @Qualifier("activeCacheRedisTemplate")
            RedisTemplate<String, Object> redisTemplate) {

        return new DefaultRedisCacheService(redisTemplate);
    }

    @Bean
    public RedisKeyGenerator redisKeyGenerator(Environment environment) {
        return new RedisKeyGenerator(environment);
    }

    @Bean
    public SpelKeyEvaluator spELKeyEvaluator() {
        return new SpelKeyEvaluator();
    }

    @Bean
    public CacheReadAspect cacheReadAspect(
            RedisCacheService redisCacheService,
            RedisKeyGenerator redisKeyGenerator,
            SpelKeyEvaluator spELKeyEvaluator) {

        return new CacheReadAspect(
                redisCacheService,
                redisKeyGenerator,
                spELKeyEvaluator
        );
    }

    @Bean
    public CacheWriteAspect cacheWriteAspect(
            RedisCacheService redisCacheService,
            RedisKeyGenerator redisKeyGenerator,
            SpelKeyEvaluator spELKeyEvaluator) {

        return new CacheWriteAspect(
                redisCacheService,
                redisKeyGenerator,
                spELKeyEvaluator
        );
    }

    @Bean
    public CacheEvictAspect cacheEvictAspect(
            RedisCacheService redisCacheService,
            RedisKeyGenerator redisKeyGenerator,
            SpelKeyEvaluator spELKeyEvaluator) {

        return new CacheEvictAspect(
                redisCacheService,
                redisKeyGenerator,
                spELKeyEvaluator
        );
    }
}