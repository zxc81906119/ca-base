package com.redhat.cleanbase.cache.config;

import com.redhat.cleanbase.cache.manager.getter.CacheManagersGetter;
import com.redhat.cleanbase.cache.resolver.MultiCacheManagerCacheResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@RequiredArgsConstructor
@EnableCaching
@Configuration
@Import(CacheInnerConfig.class)
public class CacheAutoConfig implements CachingConfigurer {

    private final CacheManagersGetter cacheManagersGetter;

    @ConditionalOnMissingBean
    @Bean
    @Override
    public CacheResolver cacheResolver() {
        return new MultiCacheManagerCacheResolver(cacheManagersGetter);
    }

}
