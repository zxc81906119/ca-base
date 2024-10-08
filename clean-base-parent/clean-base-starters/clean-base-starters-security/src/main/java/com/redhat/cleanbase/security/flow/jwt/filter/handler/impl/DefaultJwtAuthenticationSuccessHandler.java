package com.redhat.cleanbase.security.flow.jwt.filter.handler.impl;

import com.redhat.cleanbase.security.flow.jwt.cache.manager.JwtCacheManager;
import com.redhat.cleanbase.security.flow.jwt.config.properties.JwtFlowProperties;
import com.redhat.cleanbase.security.flow.jwt.datasource.impl.DefaultAccessTokenDataSource;
import com.redhat.cleanbase.security.flow.jwt.datasource.impl.DefaultRefreshTokenDataSource;
import com.redhat.cleanbase.security.flow.jwt.accessor.AuthenticationAccessor;
import com.redhat.cleanbase.security.flow.jwt.token.writer.TokenRsWriter;
import com.redhat.cleanbase.security.flow.jwt.filter.handler.JwtAuthenticationSuccessHandler;
import com.redhat.cleanbase.security.flow.jwt.generator.AbstractAccessTokenGenerator;
import com.redhat.cleanbase.security.flow.jwt.generator.AbstractRefreshTokenGenerator;
import com.redhat.cleanbase.security.flow.jwt.token.impl.DefaultAccessToken;
import com.redhat.cleanbase.security.flow.jwt.token.impl.DefaultRefreshToken;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;


public class DefaultJwtAuthenticationSuccessHandler extends JwtAuthenticationSuccessHandler<DefaultAccessToken, DefaultRefreshToken, DefaultAccessTokenDataSource, DefaultRefreshTokenDataSource> {

    public DefaultJwtAuthenticationSuccessHandler(TokenRsWriter tokenRsWriter, JwtFlowProperties jwtProperties, JwtCacheManager jwtCacheManager, AuthenticationAccessor authenticationAccessor, AbstractAccessTokenGenerator<DefaultAccessToken, DefaultAccessTokenDataSource> accessTokenGenerator, AbstractRefreshTokenGenerator<DefaultRefreshToken, DefaultRefreshTokenDataSource> refreshTokenGenerator) {
        super(tokenRsWriter, jwtProperties, jwtCacheManager, authenticationAccessor, accessTokenGenerator, refreshTokenGenerator);
    }

    @Override
    protected DefaultAccessTokenDataSource toAccessTokenDataSource(HttpServletRequest request, Authentication authentication) {
        return DefaultAccessTokenDataSource.builder()
                .userId(authentication.getName())
                .build();
    }

    @Override
    protected DefaultRefreshTokenDataSource toRefreshTokenDataSource(HttpServletRequest request, Authentication authentication, DefaultAccessToken defaultAccessToken) {
        return defaultAccessToken;
    }

}