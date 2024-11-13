package com.redhat.cleanbase.security.flow.jwt.filter;

import com.redhat.cleanbase.security.flow.jwt.accessor.AuthenticationAccessor;
import com.redhat.cleanbase.security.flow.jwt.exception.*;
import com.redhat.cleanbase.security.flow.jwt.parser.AbstractJwtParser;
import com.redhat.cleanbase.security.flow.jwt.validator.JwtValidator;
import com.redhat.cleanbase.security.flow.jwt.cache.manager.JwtCacheManager;
import com.redhat.cleanbase.security.flow.jwt.config.properties.JwtFlowProperties;
import com.redhat.cleanbase.security.flow.jwt.token.JwtToken;
import com.redhat.cleanbase.security.flow.jwt.token.getter.RqJwtTokenGetter;
import com.redhat.cleanbase.security.flow.jwt.web.JwtRequest;
import com.redhat.cleanbase.web.servlet.exception.handler.RqDelegatingExceptionHandler;
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
public abstract class JwtTokenVerifyFilter<T extends JwtToken> extends OncePerRequestFilter {

    protected final JwtFlowProperties jwtProperties;

    private final JwtValidator<T> jwtValidator;
    private final AbstractJwtParser<T> jwtParser;
    private final JwtCacheManager<?> jwtCacheManager;
    private final RqJwtTokenGetter rqJwtTokenGetter;
    private final AuthenticationAccessor<?> authenticationAccessor;
    private final RqDelegatingExceptionHandler rqDelegatingExceptionHandler;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            val tokenString = getTokenString(request);
            if (tokenString.isEmpty()) {
                filterChain.doFilter(request, response);
                return;
            }

            val jwtToken = getJwtTokenAndValidate(tokenString.get());

            val jwtCache =
                    jwtCacheManager.getCache(jwtToken)
                            .orElse(null);

            if (needCheckJwtCacheStatus()) {
                if (jwtCache == null) {
                    throw new JwtCacheNotFoundAuthenticationException();
                }
                if (jwtCache.isInvalidate()) {
                    throw new JwtCacheInvalidateAuthenticationException();
                }
            }

            val authentication = authenticationAccessor.getAuthentication(jwtToken, jwtCache);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            val finalRq =
                    jwtCache == null
                            ? request
                            : new JwtRequest(request, jwtCache);

            filterChain.doFilter(finalRq, response);

        } catch (JwtAuthenticationException e) {
            rqDelegatingExceptionHandler.handleAndWriteRs(request, response, e);
        }

    }

    protected boolean needCheckJwtCacheStatus() {
        return true;
    }

    private T getJwtTokenAndValidate(String tokenString) throws JwtParseAuthenticationException, JwtValidationAuthenticationException {
        val token = jwtParser.parse(tokenString);
        jwtValidator.validate(token);
        return token;
    }

    protected Optional<String> getTokenString(HttpServletRequest request) {
        return rqJwtTokenGetter.getToken(request);
    }

}
