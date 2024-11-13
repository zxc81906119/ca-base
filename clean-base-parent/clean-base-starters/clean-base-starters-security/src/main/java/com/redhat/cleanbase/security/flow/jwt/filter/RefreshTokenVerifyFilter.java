package com.redhat.cleanbase.security.flow.jwt.filter;

import com.redhat.cleanbase.security.config.properties.SecurityConfigProperties;
import com.redhat.cleanbase.security.flow.jwt.accessor.AuthenticationAccessor;
import com.redhat.cleanbase.security.flow.jwt.parser.AbstractJwtParser;
import com.redhat.cleanbase.security.flow.jwt.token.getter.RqJwtTokenGetter;
import com.redhat.cleanbase.security.flow.jwt.validator.JwtValidator;
import com.redhat.cleanbase.security.flow.jwt.cache.manager.JwtCacheManager;
import com.redhat.cleanbase.security.flow.jwt.config.properties.JwtFlowProperties;
import com.redhat.cleanbase.security.flow.jwt.token.RefreshToken;
import com.redhat.cleanbase.web.servlet.exception.handler.RqDelegatingExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public abstract class RefreshTokenVerifyFilter<T extends RefreshToken> extends JwtTokenVerifyFilter<T> {

    private final SecurityConfigProperties securityConfigProperties;

    public RefreshTokenVerifyFilter(JwtFlowProperties jwtProperties, JwtValidator<T> jwtValidator, AbstractJwtParser<T> jwtParser, JwtCacheManager<?> jwtCacheManager, RqJwtTokenGetter rqJwtTokenGetter, AuthenticationAccessor<?> authenticationAccessor, RqDelegatingExceptionHandler rqDelegatingExceptionHandler, SecurityConfigProperties securityConfigProperties) {
        super(jwtProperties, jwtValidator, jwtParser, jwtCacheManager, rqJwtTokenGetter, authenticationAccessor, rqDelegatingExceptionHandler);
        this.securityConfigProperties = securityConfigProperties;
    }


    @Override
    protected boolean needCheckJwtCacheStatus() {
        return false;
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        return !AntPathRequestMatcher
                .antMatcher(securityConfigProperties.getLogoutProperties().getUrl())
                .matches(request);
    }

}
