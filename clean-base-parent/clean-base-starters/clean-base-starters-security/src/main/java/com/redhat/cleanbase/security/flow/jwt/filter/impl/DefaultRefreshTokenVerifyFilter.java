package com.redhat.cleanbase.security.flow.jwt.filter.impl;

import com.redhat.cleanbase.security.config.properties.SecurityConfigProperties;
import com.redhat.cleanbase.security.flow.jwt.accessor.AuthenticationAccessor;
import com.redhat.cleanbase.security.flow.jwt.cache.manager.JwtCacheManager;
import com.redhat.cleanbase.security.flow.jwt.config.properties.JwtFlowProperties;
import com.redhat.cleanbase.security.flow.jwt.filter.RefreshTokenVerifyFilter;
import com.redhat.cleanbase.security.flow.jwt.parser.AbstractJwtParser;
import com.redhat.cleanbase.security.flow.jwt.token.getter.RqRefreshTokenGetter;
import com.redhat.cleanbase.security.flow.jwt.token.impl.DefaultRefreshToken;
import com.redhat.cleanbase.security.flow.jwt.validator.JwtValidator;
import com.redhat.cleanbase.web.servlet.exception.handler.impl.RqDelegateExceptionHandler;

public class DefaultRefreshTokenVerifyFilter extends RefreshTokenVerifyFilter<DefaultRefreshToken> {

    public DefaultRefreshTokenVerifyFilter(JwtFlowProperties jwtProperties, JwtValidator<DefaultRefreshToken> jwtValidator, AbstractJwtParser<DefaultRefreshToken> jwtParser, JwtCacheManager jwtCacheManager, RqRefreshTokenGetter rqRefreshTokenGetter, AuthenticationAccessor<?> authenticationAccessor, RqDelegateExceptionHandler rqDelegateExceptionHandler, SecurityConfigProperties securityConfigProperties) {
        super(jwtProperties, jwtValidator, jwtParser, jwtCacheManager, rqRefreshTokenGetter, authenticationAccessor, rqDelegateExceptionHandler, securityConfigProperties);
    }
}
