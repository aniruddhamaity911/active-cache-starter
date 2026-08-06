package io.github.aniruddhamaity911.activecache.annotation;

import io.github.aniruddhamaity911.activecache.config.ActiveCacheAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(ActiveCacheAutoConfiguration.class)
public @interface EnableActiveCache {
}

