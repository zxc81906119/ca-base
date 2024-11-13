package com.redhat.cleanbase.cache.manager.impl;

import com.redhat.cleanbase.web.servlet.utils.WebUtils;
import com.redhat.cleanbase.cache.manager.RequestCacheManager;
import lombok.NonNull;
import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultRequestCacheManager implements RequestCacheManager {

    private final Map<String, ThreadLocal<Cache>> cacheMap = new ConcurrentHashMap<>();

    @Override
    public Cache getCache(@NonNull String name) {
        if (!WebUtils.isWebServlet()) {
            return null;
        }
        return cacheMap.computeIfAbsent(
                        name,
                        cacheName ->
                                ThreadLocal.withInitial(() -> new ConcurrentMapCache(cacheName))
                )
                .get();
    }

    @Override
    public @NonNull Collection<String> getCacheNames() {
        return Collections.unmodifiableSet(cacheMap.keySet());
    }

    public void clearRequestCaches() {
        if (!CollectionUtils.isEmpty(cacheMap)) {
            cacheMap.values()
                    .forEach(ThreadLocal::remove);
        }
    }


}
