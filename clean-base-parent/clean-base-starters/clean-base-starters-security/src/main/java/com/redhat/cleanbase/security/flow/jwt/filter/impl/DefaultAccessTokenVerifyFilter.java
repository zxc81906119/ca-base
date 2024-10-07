package com.redhat.cleanbase.security.flow.jwt.filter.impl;

import com.redhat.cleanbase.security.flow.jwt.cache.manager.JwtCacheManager;
import com.redhat.cleanbase.security.flow.jwt.config.properties.JwtFlowProperties;
import com.redhat.cleanbase.security.flow.jwt.filter.AccessTokenVerifyFilter;
import com.redhat.cleanbase.security.flow.jwt.accessor.AuthenticationAccessor;
import com.redhat.cleanbase.security.flow.jwt.parser.AccessTokenParser;
import com.redhat.cleanbase.security.flow.jwt.token.impl.DefaultAccessToken;
import com.redhat.cleanbase.security.flow.jwt.validator.AccessTokenValidator;
import com.redhat.cleanbase.web.servlet.exception.handler.impl.RqDelegateExceptionHandler;

public class DefaultAccessTokenVerifyFilter extends AccessTokenVerifyFilter<DefaultAccessToken> {

    public DefaultAccessTokenVerifyFilter(JwtFlowProperties jwtProperties, JwtCacheManager jwtCacheManager, AccessTokenParser<DefaultAccessToken> accessTokenParser, AccessTokenValidator<DefaultAccessToken> accessTokenValidator, AuthenticationAccessor authenticationAccessor, RqDelegateExceptionHandler rqDelegateExceptionHandler) {
        super(jwtProperties, jwtCacheManager, accessTokenParser, accessTokenValidator, authenticationAccessor, rqDelegateExceptionHandler);
    }
}
