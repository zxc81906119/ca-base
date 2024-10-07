package com.redhat.cleanbase.security.flow.jwt.cache;

import com.redhat.cleanbase.security.flow.jwt.cache.func.InvalidateCacheFunc;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public abstract class AbstractJwtCache implements JwtCache {

    private final String id;
    private final long creationTime;
    private final InvalidateCacheFunc invalidateCacheFunc;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public long getCreationTime() {
        return creationTime;
    }

    @Override
    public void invalidate() {
        Optional.ofNullable(invalidateCacheFunc)
                .ifPresent(InvalidateCacheFunc::invalidate);
    }

}
