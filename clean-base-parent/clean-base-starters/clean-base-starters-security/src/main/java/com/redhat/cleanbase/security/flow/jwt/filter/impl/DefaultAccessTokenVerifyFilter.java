package com.redhat.cleanbase.security.flow.jwt.filter.impl;

import com.redhat.cleanbase.security.flow.jwt.accessor.AuthenticationAccessor;
import com.redhat.cleanbase.security.flow.jwt.cache.manager.JwtCacheManager;
import com.redhat.cleanbase.security.flow.jwt.config.properties.JwtFlowProperties;
import com.redhat.cleanbase.security.flow.jwt.filter.AccessTokenVerifyFilter;
import com.redhat.cleanbase.security.flow.jwt.parser.AbstractJwtParser;
import com.redhat.cleanbase.security.flow.jwt.token.getter.RqAccessTokenGetter;
import com.redhat.cleanbase.security.flow.jwt.token.impl.DefaultAccessToken;
import com.redhat.cleanbase.security.flow.jwt.validator.JwtValidator;
import com.redhat.cleanbase.web.servlet.exception.handler.impl.RqDelegateExceptionHandler;

public class DefaultAccessTokenVerifyFilter extends AccessTokenVerifyFilter<DefaultAccessToken> {
    public DefaultAccessTokenVerifyFilter(JwtFlowProperties jwtProperties, JwtValidator<DefaultAccessToken> jwtValidator, AbstractJwtParser<DefaultAccessToken> jwtParser, JwtCacheManager jwtCacheManager, RqAccessTokenGetter rqAccessTokenGetter, AuthenticationAccessor<?> authenticationAccessor, RqDelegateExceptionHandler rqDelegateExceptionHandler) {
        super(jwtProperties, jwtValidator, jwtParser, jwtCacheManager, rqAccessTokenGetter, authenticationAccessor, rqDelegateExceptionHandler);
    }
}
