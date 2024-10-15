package com.redhat.cleanbase.security.flow.jwt.token.getter.impl;

import com.redhat.cleanbase.security.flow.jwt.config.properties.JwtFlowProperties;
import com.redhat.cleanbase.security.flow.jwt.token.getter.RqRefreshTokenGetter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.Optional;

@RequiredArgsConstructor
public class DefaultRqRefreshTokenGetter implements RqRefreshTokenGetter {

    private final JwtFlowProperties jwtFlowProperties;

    @Override
    public Optional<String> getToken(HttpServletRequest request) {
        val rqParamName = jwtFlowProperties.getRefreshTokenProperties().getRqParamName();
        return Optional.ofNullable(request.getParameter(rqParamName))
                .filter((token) -> !token.isBlank())
                .map(String::trim);
    }
}
