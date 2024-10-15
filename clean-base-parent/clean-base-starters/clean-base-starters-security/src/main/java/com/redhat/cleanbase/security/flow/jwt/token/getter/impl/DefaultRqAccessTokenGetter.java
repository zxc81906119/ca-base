package com.redhat.cleanbase.security.flow.jwt.token.getter.impl;

import com.redhat.cleanbase.security.flow.jwt.config.properties.JwtFlowProperties;
import com.redhat.cleanbase.security.flow.jwt.token.getter.RqAccessTokenGetter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.Optional;

@RequiredArgsConstructor
public class DefaultRqAccessTokenGetter implements RqAccessTokenGetter {

    private final JwtFlowProperties jwtFlowProperties;

    @Override
    public Optional<String> getToken(HttpServletRequest request) {
        val accessTokenProperties = jwtFlowProperties.getAccessTokenProperties();
        return Optional.ofNullable(request.getHeader(accessTokenProperties.getRqHeaderName()))
                .map((headerTokenValue) -> headerTokenValue.replaceFirst(accessTokenProperties.getTokenType() + "\\s+", ""))
                .filter((jwt) -> !jwt.isBlank())
                .map(String::trim);
    }
}
