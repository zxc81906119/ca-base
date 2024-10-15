package com.redhat.cleanbase.security.flow.jwt.filter;

import com.redhat.cleanbase.security.flow.jwt.accessor.AuthenticationAccessor;
import com.redhat.cleanbase.security.flow.jwt.cache.manager.JwtCacheManager;
import com.redhat.cleanbase.security.flow.jwt.config.properties.JwtFlowProperties;
import com.redhat.cleanbase.security.flow.jwt.parser.AbstractJwtParser;
import com.redhat.cleanbase.security.flow.jwt.token.AccessToken;
import com.redhat.cleanbase.security.flow.jwt.token.getter.RqAccessTokenGetter;
import com.redhat.cleanbase.security.flow.jwt.validator.JwtValidator;
import com.redhat.cleanbase.web.servlet.exception.handler.impl.RqDelegateExceptionHandler;

public abstract class AccessTokenVerifyFilter<T extends AccessToken> extends JwtTokenVerifyFilter<T> {

    public AccessTokenVerifyFilter(JwtFlowProperties jwtProperties, JwtValidator<T> jwtValidator, AbstractJwtParser<T> jwtParser, JwtCacheManager jwtCacheManager, RqAccessTokenGetter rqAccessTokenGetter, AuthenticationAccessor<?> authenticationAccessor, RqDelegateExceptionHandler rqDelegateExceptionHandler) {
        super(jwtProperties, jwtValidator, jwtParser, jwtCacheManager, rqAccessTokenGetter, authenticationAccessor, rqDelegateExceptionHandler);
    }
}
