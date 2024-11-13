package com.redhat.cleanbase.security.flow.jwt.token.writer.impl;

import com.redhat.cleanbase.security.flow.jwt.config.properties.JwtFlowProperties;
import com.redhat.cleanbase.security.flow.jwt.token.AccessToken;
import com.redhat.cleanbase.security.flow.jwt.token.JwtToken;
import com.redhat.cleanbase.security.flow.jwt.token.RefreshToken;
import com.redhat.cleanbase.security.flow.jwt.token.model.RsTokenInfo;
import com.redhat.cleanbase.security.flow.jwt.token.writer.TokenRsWriter;
import com.redhat.cleanbase.web.model.response.GenericResponse;
import com.redhat.cleanbase.web.servlet.response.processor.RsEntityRsWriter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

@RequiredArgsConstructor
public class DefaultTokenRsWriter implements TokenRsWriter {

    protected final JwtFlowProperties jwtFlowProperties;
    private final RsEntityRsWriter rsEntityRsWriter;

    @Override
    public void write(HttpServletResponse response, AccessToken accessToken, RefreshToken refreshToken) {
        rsEntityRsWriter.lazyWriteWithSupplier(response)
                .accept(() -> toResponseEntity(accessToken, refreshToken));
    }

    protected ResponseEntity<?> toResponseEntity(AccessToken at, RefreshToken rt) {
        if (Boolean.TRUE.equals(jwtFlowProperties.getIsTokenInRsHeader())) {
            return ResponseEntity.noContent()
                    .headers((headers) ->
                            Optional.ofNullable(getTokenRsHeader(at, rt))
                                    .ifPresent(headers::putAll))
                    .build();
        }
        return ResponseEntity.ok(getTokenRsBody(at, rt));
    }

    protected RsTokenInfo getRsTokenInfo(AccessToken accessToken, RefreshToken refreshToken) {
        val accessTokenProperties = jwtFlowProperties.getAccessTokenProperties();
        val refreshTokenProperties = jwtFlowProperties.getRefreshTokenProperties();
        val builder = RsTokenInfo.builder();
        val refreshTokenOptional = Optional.ofNullable(refreshToken);
        builder
                .accessTokenParamName(accessTokenProperties.getRsParamName())
                .accessTokenExpirationParamName(accessTokenProperties.getExpirationRsParamName())
                .accessTokenExpiration(accessToken.getExpireTime().getTime())
                .refreshTokenExpiration(
                        refreshTokenOptional
                                .map(JwtToken::getExpireTime)
                                .map(Date::getTime)
                )
                .refreshTokenExpirationParamName(refreshTokenProperties.getExpirationRsParamName())
                .refreshTokenParamName(refreshTokenProperties.getRsParamName())
                .accessTokenString(accessToken.getTokenString())
                .refreshTokenString(
                        refreshTokenOptional
                                .map(JwtToken::getTokenString)
                )
        ;

        return builder.build();
    }

    protected HttpHeaders getTokenRsHeader(AccessToken at, RefreshToken rt) {
        val rsTokenInfo = getRsTokenInfo(at, rt);
        val httpHeaders = new HttpHeaders();
        httpHeaders.set(rsTokenInfo.getAccessTokenParamName(), rsTokenInfo.getAccessTokenString());
        httpHeaders.set(rsTokenInfo.getAccessTokenExpirationParamName(), rsTokenInfo.getAccessTokenExpiration().toString());
        rsTokenInfo.getRefreshTokenString()
                .ifPresent((refreshToken) -> httpHeaders.set(rsTokenInfo.getRefreshTokenParamName(), refreshToken));
        rsTokenInfo.getRefreshTokenExpiration()
                .ifPresent((refreshTokenExpiration) -> httpHeaders.set(rsTokenInfo.getRefreshTokenExpirationParamName(), refreshTokenExpiration.toString()));
        return httpHeaders;
    }

    protected Object getTokenRsBody(AccessToken at, RefreshToken rt) {
        val rsTokenInfo = getRsTokenInfo(at, rt);
        val map = new HashMap<>();
        map.put(rsTokenInfo.getAccessTokenParamName(), rsTokenInfo.getAccessTokenString());
        map.put(rsTokenInfo.getAccessTokenExpirationParamName(), rsTokenInfo.getAccessTokenExpiration().toString());
        rsTokenInfo.getRefreshTokenString()
                .ifPresent((refreshTokenString) -> map.put(rsTokenInfo.getRefreshTokenParamName(), refreshTokenString));
        rsTokenInfo.getRefreshTokenExpiration()
                .ifPresent((refreshTokenExpiration) -> map.put(rsTokenInfo.getRefreshTokenExpirationParamName(), refreshTokenExpiration.toString()));
        return GenericResponse.ok(map);
    }
}
