package com.redhat.cleanbase.security.flow.jwt.accessor;

import com.redhat.cleanbase.security.flow.jwt.cache.JwtCache;
import com.redhat.cleanbase.security.flow.jwt.token.AccessToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface AuthenticationAccessor<U extends UserDetails> {

    Map<String, Object> getAuthInfoForCache(Authentication authentication);

    Authentication getAuthentication(AccessToken accessToken, JwtCache jwtCache);

    Authentication createAuthentication(U u);
}
