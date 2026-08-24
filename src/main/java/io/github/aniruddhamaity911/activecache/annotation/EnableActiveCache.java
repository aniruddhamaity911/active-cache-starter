package io.github.aniruddhamaity911.activecache.annotation;

import io.github.aniruddhamaity911.activecache.config.ActiveCacheAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(ActiveCacheAutoConfiguration.class)
@ConditionalOnBean(RedisConnectionFactory.class)
public @interface EnableActiveCache {
}

