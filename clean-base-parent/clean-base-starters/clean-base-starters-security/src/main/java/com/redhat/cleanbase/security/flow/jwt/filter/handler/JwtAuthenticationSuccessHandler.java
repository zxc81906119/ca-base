package com.redhat.cleanbase.security.flow.jwt.filter.handler;

import com.redhat.cleanbase.security.flow.jwt.cache.manager.JwtCacheManager;
import com.redhat.cleanbase.security.flow.jwt.config.properties.JwtFlowProperties;
import com.redhat.cleanbase.security.flow.jwt.datasource.AccessTokenDataSource;
import com.redhat.cleanbase.security.flow.jwt.datasource.RefreshTokenDataSource;
import com.redhat.cleanbase.security.flow.jwt.accessor.AuthenticationAccessor;
import com.redhat.cleanbase.security.flow.jwt.token.writer.RsTokenWriter;
import com.redhat.cleanbase.security.flow.jwt.generator.AbstractAccessTokenGenerator;
import com.redhat.cleanbase.security.flow.jwt.generator.AbstractRefreshTokenGenerator;
import com.redhat.cleanbase.security.flow.jwt.token.AccessToken;
import com.redhat.cleanbase.security.flow.jwt.token.RefreshToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;


public abstract class JwtAuthenticationSuccessHandler<AT extends AccessToken, RT extends RefreshToken, ATS extends AccessTokenDataSource, RTS extends RefreshTokenDataSource> implements AuthenticationSuccessHandler {

    protected final JwtFlowProperties jwtProperties;

    private final RsTokenWriter rsTokenWriter;
    private final JwtCacheManager jwtCacheManager;
    private final AuthenticationAccessor<?> authenticationAccessor;
    private final AbstractAccessTokenGenerator<AT, ATS> accessTokenGenerator;
    private final AbstractRefreshTokenGenerator<RT, RTS> refreshTokenGenerator;

    public JwtAuthenticationSuccessHandler(
            RsTokenWriter rsTokenWriter,
            JwtFlowProperties jwtProperties,
            JwtCacheManager jwtCacheManager,
            AuthenticationAccessor<?> authenticationAccessor,
            AbstractAccessTokenGenerator<AT, ATS> accessTokenGenerator,
            AbstractRefreshTokenGenerator<RT, RTS> refreshTokenGenerator
    ) {
        this.rsTokenWriter = rsTokenWriter;
        this.jwtProperties = jwtProperties;
        this.jwtCacheManager = jwtCacheManager;
        this.accessTokenGenerator = accessTokenGenerator;
        this.refreshTokenGenerator = refreshTokenGenerator;
        this.authenticationAccessor = authenticationAccessor;
    }

    @SneakyThrows
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication
    ) throws IOException, ServletException {
        val at = genAccessToken(request, authentication);
        val rt = genRefreshToken(request, authentication, at);
        val jwtCache = jwtCacheManager.createCache(rt);
        val authInfo = authenticationAccessor.getAuthInfoForCache(authentication);
        jwtCache.setAttributes(authInfo);
        rsTokenWriter.write(response, at, rt);
    }

    private RT genRefreshToken(HttpServletRequest request,
                               Authentication authentication,
                               AT at
    ) {
        val rts = toRefreshTokenDataSource(request, authentication, at);
        return refreshTokenGenerator.generate(rts);
    }

    protected abstract RTS toRefreshTokenDataSource(HttpServletRequest request, Authentication authentication, AT at);

    private AT genAccessToken(HttpServletRequest request,
                              Authentication authentication
    ) {
        val ats = toAccessTokenDataSource(request, authentication);
        return accessTokenGenerator.generate(ats);
    }

    protected abstract ATS toAccessTokenDataSource(HttpServletRequest request, Authentication authentication);

}