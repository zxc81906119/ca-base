package com.redhat.cleanbase.security.flow.jwt.cache.manager.impl;

import com.redhat.cleanbase.security.flow.jwt.exception.JwtCacheCreateAuthenticationException;
import com.redhat.cleanbase.security.flow.jwt.cache.manager.JwtCacheManager;
import com.redhat.cleanbase.security.flow.jwt.token.JwtToken;
import com.redhat.cleanbase.security.flow.jwt.cache.JwtCache;
import com.redhat.cleanbase.security.flow.jwt.cache.impl.DefaultJwtCache;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class DefaultJwtCacheManager implements JwtCacheManager {

    private final Map<String, DefaultJwtCache> cache = new ConcurrentHashMap<>();

    @Override
    public String toUniqueId(JwtToken jwtToken) {
        return jwtToken.getId();
    }

    @Override
    public JwtCache createCache(@NonNull JwtToken jwtToken) throws JwtCacheCreateAuthenticationException {
        return tokenOperate(
                jwtToken,
                (uniqueId) -> {
                    if (cache.containsKey(uniqueId)) {
                        throw new JwtCacheCreateAuthenticationException("不得有重複唯一標示");
                    }

                    synchronized (cache) {
                        if (cache.containsKey(uniqueId)) {
                            throw new JwtCacheCreateAuthenticationException("不得有重複唯一標示");
                        }

                        val cacheSpace = new DefaultJwtCache(
                                uniqueId,
                                jwtToken.getExpireTime().getTime(),
                                jwtToken.getCreationTime().getTime(),
                                jwtToken.getPayload(),
                                () -> cache.remove(uniqueId)
                        );
                        cache.put(uniqueId, cacheSpace);
                        return cacheSpace;
                    }
                }
        );
    }

    @Override
    public Optional<JwtCache> getCache(JwtToken jwtToken) {
        return tokenOperate(
                jwtToken,
                (uniqueId) ->
                        Optional.ofNullable(cache.get(uniqueId))
        );
    }

}
