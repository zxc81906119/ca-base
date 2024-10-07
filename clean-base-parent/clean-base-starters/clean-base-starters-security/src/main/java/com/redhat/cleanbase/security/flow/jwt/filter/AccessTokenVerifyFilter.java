package com.redhat.cleanbase.security.flow.jwt.filter;

import com.redhat.cleanbase.security.flow.jwt.accessor.AuthenticationAccessor;
import com.redhat.cleanbase.security.flow.jwt.cache.manager.JwtCacheManager;
import com.redhat.cleanbase.security.flow.jwt.config.properties.JwtFlowProperties;
import com.redhat.cleanbase.security.flow.jwt.exception.*;
import com.redhat.cleanbase.security.flow.jwt.parser.AccessTokenParser;
import com.redhat.cleanbase.security.flow.jwt.token.AccessToken;
import com.redhat.cleanbase.security.flow.jwt.validator.AccessTokenValidator;
import com.redhat.cleanbase.security.flow.jwt.web.JwtRequest;
import com.redhat.cleanbase.web.servlet.exception.handler.impl.RqDelegateExceptionHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class AccessTokenVerifyFilter<T extends AccessToken> extends OncePerRequestFilter {

    private final JwtFlowProperties jwtProperties;
    private final JwtCacheManager jwtCacheManager;
    private final AccessTokenParser<T> accessTokenParser;
    private final AccessTokenValidator<T> accessTokenValidator;
    private final AuthenticationAccessor<?> authenticationAccessor;
    private final RqDelegateExceptionHandler rqDelegateExceptionHandler;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            val accessTokenString = getAccessTokenString(request);
            if (accessTokenString.isEmpty()) {
                filterChain.doFilter(request, response);
                return;
            }

            val accessToken = getAccessTokenAndValidate(accessTokenString.get());

            val jwtCache =
                    jwtCacheManager.getCache(accessToken)
                            .orElseThrow(JwtCacheNotFoundAuthenticationException::new);
            if (jwtCache.isInvalidate()) {
                throw new JwtCacheInvalidateAuthenticationException();
            }

            val authentication = authenticationAccessor.getAuthentication(accessToken, jwtCache);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            val jwtRequest = new JwtRequest(request, jwtCache);
            filterChain.doFilter(jwtRequest, response);

        } catch (JwtAuthenticationException e) {
            rqDelegateExceptionHandler.handleAndWriteRs(request, response, e);
        }

    }

    private T getAccessTokenAndValidate(String accessTokenString) throws JwtParseAuthenticationException, JwtValidationAuthenticationException {
        val accessToken = accessTokenParser.parse(accessTokenString);
        accessTokenValidator.validate(accessToken);
        return accessToken;
    }

    protected Optional<String> getAccessTokenString(HttpServletRequest request) {
        val accessTokenProperties = jwtProperties.getAccessTokenProperties();
        return Optional.ofNullable(request.getHeader(accessTokenProperties.getRqHeaderName()))
                .map((headerTokenValue) -> headerTokenValue.replaceFirst(accessTokenProperties.getTokenType() + "\\s+", ""))
                .filter((jwt) -> !jwt.isBlank())
                .map(String::trim);
    }

}
