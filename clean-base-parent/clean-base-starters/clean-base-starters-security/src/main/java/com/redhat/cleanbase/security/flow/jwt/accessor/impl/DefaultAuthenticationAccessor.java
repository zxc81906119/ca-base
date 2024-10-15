package com.redhat.cleanbase.security.flow.jwt.accessor.impl;

import com.redhat.cleanbase.common.utils.CastUtils;
import com.redhat.cleanbase.security.flow.jwt.accessor.AuthenticationAccessor;
import com.redhat.cleanbase.security.flow.jwt.cache.JwtCache;
import com.redhat.cleanbase.security.flow.jwt.filter.model.impl.LoginAuthToken;
import com.redhat.cleanbase.security.flow.jwt.token.JwtToken;
import lombok.val;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import java.util.Map;

public class DefaultAuthenticationAccessor implements AuthenticationAccessor<User> {

    public static final String USER = "user";

    @Override
    public Map<String, Object> getAuthInfoForCache(Authentication authentication) {
        return Map.of(
                USER, authentication.getPrincipal()
        );
    }

    @Override
    public Authentication getAuthentication(JwtToken jwtToken, JwtCache jwtCache) {
        if (jwtCache == null) {
            return null;
        }
        val u = CastUtils.cast(jwtCache.getAttribute(USER), User.class);
        if (u == null) {
            throw new RuntimeException("user 莫名消失 , 請找 JwtCacheManager 之實作者發洩");
        }
        return new LoginAuthToken(u, u.getPassword(), u.getAuthorities());
    }

    @Override
    public Authentication createAuthentication(User u) {
        return new LoginAuthToken(u, u.getPassword(), u.getAuthorities());
    }

}
