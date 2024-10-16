package com.redhat.cleanbase.cache.manager.condition;

import com.redhat.cleanbase.cache.manager.RequestCacheManager;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;

import java.util.Collection;

@RequiredArgsConstructor
public abstract class RequestCacheManagerCondition implements CacheManagerCondition {

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