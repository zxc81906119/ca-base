package com.redhat.cleanbase.cache.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.redhat.cleanbase.cache.consts.CacheConstants;
import com.redhat.cleanbase.cache.manager.RequestCacheManager;
import com.redhat.cleanbase.cache.manager.condition.RequestCacheManagerCondition;
import com.redhat.cleanbase.cache.manager.condition.impl.DefaultRequestCacheManagerCondition;
import com.redhat.cleanbase.cache.manager.getter.CacheManagersGetter;
import com.redhat.cleanbase.cache.manager.impl.DefaultRequestCacheManager;
import com.redhat.cleanbase.cache.manager.listener.CustomServletRequestListener;
import jakarta.servlet.ServletRequestListener;
import lombok.val;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

@Configuration
public class CacheInnerConfig {

    public static final String LOCAL_CACHE_MANAGER = "LOCAL_CACHE_MANAGER";

    @ConditionalOnMissingBean(name = LOCAL_CACHE_MANAGER)
    @Bean(LOCAL_CACHE_MANAGER)
    public CacheManager localCacheManager() {
        val manager = new SimpleCacheManager();
        manager.setCaches(
                List.of(
                        buildCaffeineCache(CacheConstants.CacheName.LOCAL_TEN_SEC, Duration.ofSeconds(10)),
                        buildCaffeineCache(CacheConstants.CacheName.LOCAL_ONE_MIN, Duration.ofMinutes(1)),
                        buildCaffeineCache(CacheConstants.CacheName.LOCAL_FIVE_MIN, Duration.ofMinutes(5)),
                        buildCaffeineCache(CacheConstants.CacheName.LOCAL_TEN_MIN, Duration.ofMinutes(30)),
                        buildCaffeineCache(CacheConstants.CacheName.LOCAL_ONE_HOUR, Duration.ofHours(1)),
                        buildCaffeineCache(CacheConstants.CacheName.LOCAL_ONE_DAY, Duration.ofDays(1))
                )
        );
        return manager;
    }

    private CaffeineCache buildCaffeineCache(String name, Duration duration) {
        return new CaffeineCache(name, Caffeine.newBuilder().expireAfterWrite(duration).build());
    }

    @ConditionalOnMissingBean
    @Bean
    public CacheManagersGetter cacheManagersGetter(
            ObjectProvider<RequestCacheManagerCondition> conditionObjectProvider,
            @Qualifier(LOCAL_CACHE_MANAGER) CacheManager localCacheManager
    ) {
        val condition = conditionObjectProvider.getIfAvailable();
        val cacheManagers =
                condition != null ?
                        List.of(condition, localCacheManager)
                        : List.of(localCacheManager);
        return () -> cacheManagers;
    }


    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    static class ServletConfig {

        @ConditionalOnMissingBean
        @Bean
        public RequestCacheManager requestCacheManager() {
            return new DefaultRequestCacheManager();
        }

        @ConditionalOnMissingBean
        @Bean
        public RequestCacheManagerCondition requestCacheManagerCondition(RequestCacheManager requestCacheManager) {
            return new DefaultRequestCacheManagerCondition(requestCacheManager);
        }

        @Bean
        public ServletRequestListener customServletRequestListener(RequestCacheManager requestCacheManager) {
            return new CustomServletRequestListener(requestCacheManager);
        }

    }


}
