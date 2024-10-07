package com.redhat.cleanbase.security.flow.jwt.filter;

import com.redhat.cleanbase.security.flow.jwt.cache.JwtCache;
import com.redhat.cleanbase.security.flow.jwt.cache.manager.JwtCacheManager;
import com.redhat.cleanbase.security.flow.jwt.config.properties.JwtFlowProperties;
import com.redhat.cleanbase.security.flow.jwt.datasource.AccessTokenDataSource;
import com.redhat.cleanbase.security.flow.jwt.datasource.RefreshTokenDataSource;
import com.redhat.cleanbase.security.flow.jwt.exception.*;
import com.redhat.cleanbase.security.flow.jwt.generator.AbstractAccessTokenGenerator;
import com.redhat.cleanbase.security.flow.jwt.generator.AbstractRefreshTokenGenerator;
import com.redhat.cleanbase.security.flow.jwt.lock.ResourceLock;
import com.redhat.cleanbase.security.flow.jwt.parser.RefreshTokenParser;
import com.redhat.cleanbase.security.flow.jwt.token.AccessToken;
import com.redhat.cleanbase.security.flow.jwt.token.RefreshToken;
import com.redhat.cleanbase.security.flow.jwt.token.writer.RsTokenWriter;
import com.redhat.cleanbase.security.flow.jwt.validator.RefreshTokenValidator;
import com.redhat.cleanbase.web.servlet.exception.handler.impl.RqDelegateExceptionHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public abstract class RefreshTokenFilter<AT extends AccessToken, ATS extends AccessTokenDataSource, RT extends RefreshToken, RTS extends RefreshTokenDataSource> extends OncePerRequestFilter {

    private final ResourceLock resourceLock;
    private final RsTokenWriter rsTokenWriter;
    private final JwtCacheManager jwtCacheManager;
    private final JwtFlowProperties jwtProperties;
    private final RefreshTokenParser<RT> refreshTokenParser;
    private final RefreshTokenValidator<RT> refreshTokenValidator;
    private final RqDelegateExceptionHandler rqDelegateExceptionHandler;
    private final AbstractAccessTokenGenerator<AT, ATS> accessTokenGenerator;
    private final AbstractRefreshTokenGenerator<RT, RTS> refreshTokenGenerator;

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        return !AntPathRequestMatcher
                .antMatcher(HttpMethod.POST, jwtProperties.getRefreshTokenProperties().getUri())
                .matches(request);
    }

    protected abstract Optional<String> getRefreshTokenString(HttpServletRequest request);

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) {
        try {
            // todo 實做
            val refreshTokenString = getRefreshTokenString(request);
            if (refreshTokenString.isEmpty()) {
                throw new RefreshTokenNotFoundAuthenticationException();
            }

            val rqRefreshToken = getRefreshTokenAndValidate(refreshTokenString.get());

            val jwtCache =
                    jwtCacheManager.getCache(rqRefreshToken)
                            .orElseThrow(JwtCacheNotFoundAuthenticationException::new);
            if (jwtCache.isInvalidate()) {
                throw new JwtCacheInvalidateAuthenticationException();
            }

            if (needRenewRefreshToken(rqRefreshToken)) {
                refreshBothToken(response, rqRefreshToken, jwtCache);
                return;
            }

            refreshAccessToken(response, rqRefreshToken);

        } catch (JwtAuthenticationException e) {
            rqDelegateExceptionHandler.handleAndWriteRs(request, response, e);
        }
    }

    private void refreshAccessToken(HttpServletResponse response, RT rqRefreshToken) {
        val newAccessToken = genAccessTokenWithSameId(rqRefreshToken);
        rsTokenWriter.write(response, newAccessToken, null);
    }

    private void refreshBothToken(HttpServletResponse response, RT rqRefreshToken, JwtCache jwtCache) throws JwtCacheInvalidateAuthenticationException, JwtCacheCreateAuthenticationException {
        val attrs = getAttrsAndInvalidate(jwtCache);
        val newAccessToken = genAccessTokenWithNewId(rqRefreshToken);
        val newRefreshToken = genRefreshToken(newAccessToken);
        createJwtCacheAndSetAttrs(attrs, newRefreshToken);
        rsTokenWriter.write(response, newAccessToken, newRefreshToken);
    }

    private void createJwtCacheAndSetAttrs(Map<String, Object> attrs, RT newRefreshToken) throws JwtCacheCreateAuthenticationException {
        val newJwtCacheSpace = jwtCacheManager.createCache(newRefreshToken);
        newJwtCacheSpace.setAttributes(attrs);
    }

    private RT getRefreshTokenAndValidate(String refreshTokenString) throws JwtParseAuthenticationException, JwtValidationAuthenticationException {
        val inputRefreshToken = refreshTokenParser.parse(refreshTokenString);
        refreshTokenValidator.validate(inputRefreshToken);
        return inputRefreshToken;
    }

    private AT genAccessTokenWithSameId(RT inputRefreshToken) {
        val accessTokenDataSource = getAccessTokenDataSourceWithSameId(inputRefreshToken);
        return accessTokenGenerator.generate(accessTokenDataSource);
    }

    private RT genRefreshToken(AT newAccessToken) {
        val refreshTokenDataSource = getRefreshTokenDataSource(newAccessToken);
        return refreshTokenGenerator.generate(refreshTokenDataSource);
    }

    private AT genAccessTokenWithNewId(RT rqRefreshToken) {
        val accessTokenDataSource = getAccessTokenDataSourceWithNewId(rqRefreshToken);
        return accessTokenGenerator.generate(accessTokenDataSource);
    }

    private Map<String, Object> getAttrsAndInvalidate(JwtCache jwtCache) throws JwtCacheInvalidateAuthenticationException {
        return resourceLock.lock(
                jwtCache.getId(),
                () -> {
                    if (jwtCache.isInvalidate()) {
                        throw new JwtCacheInvalidateAuthenticationException();
                    }
                    try {
                        return jwtCache.getAttributes();
                    } finally {
                        jwtCache.invalidate();
                    }
                });
    }

    protected abstract ATS getAccessTokenDataSourceWithSameId(RT rqRefreshToken);

    protected abstract ATS getAccessTokenDataSourceWithNewId(RT rqRefreshToken);

    protected abstract RTS getRefreshTokenDataSource(AT newAccessToken);

    protected boolean needRenewRefreshToken(RT refreshToken) {
        val isRenewRefreshToken = jwtProperties.getRefreshTokenProperties().getIsRenew();
        if (isRenewRefreshToken != null) {
            return isRenewRefreshToken;
        }

        val renewRefreshTokenTimeUntilExpirationInterval = jwtProperties.getRenewRefreshTokenTimeUntilExpirationInterval();
        if (renewRefreshTokenTimeUntilExpirationInterval == null) {
            return true;
        }
        val time = renewRefreshTokenTimeUntilExpirationInterval.getTime();
        if (time == null) {
            return true;
        }
        val timeUnit =
                Optional.ofNullable(renewRefreshTokenTimeUntilExpirationInterval.getTimeUnit())
                        .orElse(TimeUnit.MINUTES);

        return refreshToken.getExpireTime()
                .before(new Date(System.currentTimeMillis() + timeUnit.toMillis(time)));
    }
}
