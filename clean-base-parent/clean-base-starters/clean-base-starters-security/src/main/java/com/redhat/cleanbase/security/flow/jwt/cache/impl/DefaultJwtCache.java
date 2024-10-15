package com.redhat.cleanbase.security.flow.jwt.cache.impl;


import com.redhat.cleanbase.security.flow.jwt.cache.func.InvalidateCacheFunc;
import com.redhat.cleanbase.security.flow.jwt.lock.DataWithReadWriteLock;
import com.redhat.cleanbase.security.flow.jwt.cache.AbstractJwtCache;
import lombok.NonNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultJwtCache extends AbstractJwtCache {

    private final Map<String, Object> payload = new HashMap<>();
    private final Map<String, Object> cache = new ConcurrentHashMap<>();

    private final DataWithReadWriteLock<Boolean> isInvalidate = new DataWithReadWriteLock<>(false);
    private final DataWithReadWriteLock<Long> lastAccessTime = new DataWithReadWriteLock<>();
    private final DataWithReadWriteLock<Long> expireTime = new DataWithReadWriteLock<>();

    public DefaultJwtCache(
            String id,
            long expireTimeMillis,
            long creationTimeMillis,
            Map<String, Object> payload,
            InvalidateCacheFunc invalidateCacheFunc
    ) {
        super(id, creationTimeMillis, invalidateCacheFunc);
        lastAccessTime.setData(creationTimeMillis);
        expireTime.setData(expireTimeMillis);
        Optional.ofNullable(payload)
                .ifPresent(this.payload::putAll);
    }


    @Override
    public long getExpireTime() {
        return expireTime.getData();
    }

    @Override
    public void setExpireTime(long expireTime) {
        this.expireTime.setData(expireTime);
    }

    @Override
    public Set<String> getAttributeNames() {
        return Collections.unmodifiableSet(cache.keySet());
    }

    public Map<String, Object> getAttributes() {
        return new HashMap<>(cache);
    }

    @Override
    public void setAttributes(Map<String, Object> map) {
        Optional.ofNullable(map)
                .ifPresent(cache::putAll);
    }

    @Override
    public void setAttribute(@NonNull String name, Object value) {
        cache.put(name, value);
    }

    @Override
    public Object getAttribute(@NonNull String name) {
        return Optional.ofNullable(cache.get(name))
                .orElseGet(() -> payload.get(name));
    }

    @Override
    public void removeAttribute(@NonNull String name) {
        cache.remove(name);
    }

    @Override
    public boolean isInvalidate() {
        return isInvalidate.getData();
    }

    @Override
    public long getLastAccessedTime() {
        return lastAccessTime.getData();
    }

    @Override
    public void setLastAccessedTime(long lastAccessedTime) {
        lastAccessTime.setData(lastAccessedTime);
    }

    @Override
    public void invalidate() {
        if (isInvalidate()) {
            return;
        }
        isInvalidate.doSomething(() -> {
            if (isInvalidate()) {
                return;
            }
            try {
                super.invalidate();
            } finally {
                isInvalidate.setData(true);
            }
        });
    }
}
