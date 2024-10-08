package com.redhat.cleanbase.security.flow.jwt.filter.impl;

import com.redhat.cleanbase.security.flow.jwt.cache.manager.JwtCacheManager;
import com.redhat.cleanbase.security.flow.jwt.config.properties.JwtFlowProperties;
import com.redhat.cleanbase.security.flow.jwt.datasource.impl.DefaultAccessTokenDataSource;
import com.redhat.cleanbase.security.flow.jwt.datasource.impl.DefaultRefreshTokenDataSource;
import com.redhat.cleanbase.security.flow.jwt.filter.RefreshTokenFilter;
import com.redhat.cleanbase.security.flow.jwt.token.writer.TokenRsWriter;
import com.redhat.cleanbase.security.flow.jwt.generator.AbstractAccessTokenGenerator;
import com.redhat.cleanbase.security.flow.jwt.generator.AbstractRefreshTokenGenerator;
import com.redhat.cleanbase.security.flow.jwt.lock.ResourceLock;
import com.redhat.cleanbase.security.flow.jwt.parser.RefreshTokenParser;
import com.redhat.cleanbase.security.flow.jwt.token.impl.DefaultAccessToken;
import com.redhat.cleanbase.security.flow.jwt.token.impl.DefaultRefreshToken;
import com.redhat.cleanbase.security.flow.jwt.validator.RefreshTokenValidator;
import com.redhat.cleanbase.web.servlet.exception.handler.impl.RqDelegateExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class DefaultRefreshTokenFilter extends RefreshTokenFilter<DefaultAccessToken, DefaultAccessTokenDataSource, DefaultRefreshToken, DefaultRefreshTokenDataSource> {

    public DefaultRefreshTokenFilter(ResourceLock resourceLock, TokenRsWriter tokenRsWriter, JwtCacheManager jwtCacheManager, JwtFlowProperties jwtProperties, RefreshTokenParser<DefaultRefreshToken> refreshTokenParser, RefreshTokenValidator<DefaultRefreshToken> refreshTokenValidator, RqDelegateExceptionHandler rqDelegateExceptionHandler, AbstractAccessTokenGenerator<DefaultAccessToken, DefaultAccessTokenDataSource> accessTokenGenerator, AbstractRefreshTokenGenerator<DefaultRefreshToken, DefaultRefreshTokenDataSource> refreshTokenGenerator) {
        super(resourceLock, tokenRsWriter, jwtCacheManager, jwtProperties, refreshTokenParser, refreshTokenValidator, rqDelegateExceptionHandler, accessTokenGenerator, refreshTokenGenerator);
    }

    @Override
    protected Optional<String> getRefreshTokenString(HttpServletRequest request) {
        // todo
        return Optional.empty();
    }

    @Override
    protected DefaultAccessTokenDataSource getAccessTokenDataSourceWithSameId(DefaultRefreshToken rqRefreshToken) {
        return DefaultAccessTokenDataSource.builder()
                .jwtId(rqRefreshToken.getId())
                .userId(rqRefreshToken.getSubject())
                .build();
    }

    @Override
    protected DefaultAccessTokenDataSource getAccessTokenDataSourceWithNewId(DefaultRefreshToken rqRefreshToken) {
        return DefaultAccessTokenDataSource.builder()
                .userId(rqRefreshToken.getSubject())
                .build();
    }

    @Override
    protected DefaultRefreshTokenDataSource getRefreshTokenDataSource(DefaultAccessToken newAccessToken) {
        return newAccessToken;
    }
}
