package com.redhat.cleanbase.cache.config;

import com.redhat.cleanbase.cache.manager.request.listener.CustomServletRequestListener;
import com.redhat.cleanbase.cache.manager.request.RequestCacheManager;
import com.redhat.cleanbase.cache.manager.request.impl.DefaultRequestCacheManager;
import com.redhat.cleanbase.cache.manager.condition.CacheManagerCondition;
import com.redhat.cleanbase.cache.manager.getter.CacheManagersGetter;
import jakarta.servlet.ServletRequestListener;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
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

                )
        );
        return manager;
    }

    @ConditionalOnBean(RequestCacheManagerCondition.class)
    @ConditionalOnMissingBean
    @Bean
    public CacheManagersGetter cacheManagersGetter(
            RequestCacheManagerCondition condition,
            @Qualifier(LOCAL_CACHE_MANAGER) CacheManager localCacheManager
    ) {
        return () -> List.of(condition, localCacheManager);
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


    @RequiredArgsConstructor
    public abstract static class RequestCacheManagerCondition implements CacheManagerCondition {

        public static final String RQ_CACHE_PREFIX = "rq:";

        @NonNull
        private final RequestCacheManager requestCacheManager;

        @Override
        public String getCacheNamePrefix() {
            return RQ_CACHE_PREFIX;
        }

        @Override
        public Cache getCache(@NonNull String name) {
            return requestCacheManager.getCache(name);
        }

        @Override
        public @NonNull Collection<String> getCacheNames() {
            return requestCacheManager.getCacheNames();
        }
    }

    public static class DefaultRequestCacheManagerCondition extends RequestCacheManagerCondition {

        public DefaultRequestCacheManagerCondition(@NonNull RequestCacheManager requestCacheManager) {
            super(requestCacheManager);
        }

    }


}
