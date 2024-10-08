package com.redhat.cleanbase.security.flow.jwt.cache.manager;


import com.redhat.cleanbase.common.func.Operator;
import com.redhat.cleanbase.security.flow.jwt.cache.JwtCache;
import com.redhat.cleanbase.security.flow.jwt.exception.JwtCacheCreateAuthenticationException;
import com.redhat.cleanbase.security.flow.jwt.token.JwtToken;
import lombok.val;

import java.util.Optional;

public interface JwtCacheManager {

    default <O, E extends Exception> O cacheOperate(JwtToken jwtToken, Operator<String, O, E> operator) throws E {
        val uniqueId = toUniqueId(jwtToken);
        return operator.operate(uniqueId);
    }

    String toUniqueId(JwtToken jwtToken);

    JwtCache createCache(JwtToken jwtToken) throws JwtCacheCreateAuthenticationException;

    Optional<JwtCache> getCache(JwtToken jwtToken);

}
