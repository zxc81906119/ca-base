package com.redhat.cleanbase.security.flow.jwt.token.writer.impl;

import com.redhat.cleanbase.security.flow.jwt.config.properties.JwtFlowProperties;
import com.redhat.cleanbase.security.flow.jwt.token.AccessToken;
import com.redhat.cleanbase.security.flow.jwt.token.JwtToken;
import com.redhat.cleanbase.security.flow.jwt.token.RefreshToken;
import com.redhat.cleanbase.security.flow.jwt.token.model.RsTokenInfo;
import com.redhat.cleanbase.security.flow.jwt.token.writer.TokenRsWriter;
import com.redhat.cleanbase.web.servlet.response.processor.RsEntityRsWriter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class DefaultTokenRsWriter implements TokenRsWriter {

    private final JwtFlowProperties jwtFlowProperties;
    private final RsEntityRsWriter rsEntityRsWriter;

    @Override
    public void write(HttpServletResponse response, AccessToken accessToken, RefreshToken refreshToken) {
        rsEntityRsWriter.lazyWriteWithSupplier(response)
                .accept(() -> toResponseEntity(accessToken, refreshToken));
    }

    protected ResponseEntity<?> toResponseEntity(AccessToken at, RefreshToken rt) {

        val accessTokenParamName =
                jwtFlowProperties.getAccessTokenProperties()
                        .getRsParamName();
        val refreshTokenParamName =
                jwtFlowProperties.getRefreshTokenProperties()
                        .getRsParamName();

        val accessTokenString = at.getTokenString();
        val refreshTokenString =
                Optional.ofNullable(rt)
                        .map(JwtToken::getTokenString)
                        .orElse(null);
        if (Boolean.TRUE.equals(jwtFlowProperties.getIsTokenInRsHeader())) {
            return ResponseEntity.noContent()
                    .headers((headers) -> {
                        headers.set(accessTokenParamName, accessTokenString);
                        headers.set(refreshTokenParamName, refreshTokenString);
                    })
                    .build();
        }

        val rsTokenInfo = RsTokenInfo.builder()
                .accessTokenParamName(accessTokenParamName)
                .accessTokenString(accessTokenString)
                .refreshTokenString(refreshTokenString)
                .refreshTokenParamName(refreshTokenParamName)
                .build();
        return ResponseEntity.ok(getTokenRsBody(rsTokenInfo));
    }

    protected Object getTokenRsBody(RsTokenInfo rsTokenInfo) {
        return Map.of(
                rsTokenInfo.getAccessTokenParamName(), rsTokenInfo.getAccessTokenString(),
                rsTokenInfo.getRefreshTokenParamName(), rsTokenInfo.getRefreshTokenString()
        );
    }
}
