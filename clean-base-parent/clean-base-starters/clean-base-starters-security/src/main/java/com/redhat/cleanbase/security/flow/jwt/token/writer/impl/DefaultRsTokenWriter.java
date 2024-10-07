package com.redhat.cleanbase.security.flow.jwt.token.writer.impl;

import com.redhat.cleanbase.security.flow.jwt.config.properties.JwtFlowProperties;
import com.redhat.cleanbase.security.flow.jwt.token.AccessToken;
import com.redhat.cleanbase.security.flow.jwt.token.RefreshToken;
import com.redhat.cleanbase.security.flow.jwt.token.model.RsTokenInfo;
import com.redhat.cleanbase.security.flow.jwt.token.writer.RsTokenWriter;
import com.redhat.cleanbase.web.servlet.response.processor.WriteRsEntityToRsProcessor;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@RequiredArgsConstructor
public class DefaultRsTokenWriter implements RsTokenWriter {

    private final JwtFlowProperties jwtFlowProperties;
    private final WriteRsEntityToRsProcessor writeRsEntityToRsProcessor;

    @Override
    public void write(HttpServletResponse response, AccessToken accessToken, RefreshToken refreshToken) {
        writeRsEntityToRsProcessor.lazyWriteRsWithRsEntitySupplier(response)
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
        val refreshTokenString = rt.getTokenString();

        val bodyBuilder = ResponseEntity.ok();
        if (Boolean.TRUE.equals(jwtFlowProperties.getIsTokenInRsHeader())) {
            bodyBuilder.headers((headers) -> {
                headers.set(accessTokenParamName, accessTokenString);
                headers.set(refreshTokenParamName, refreshTokenString);
            });
        } else {
            val rsTokenInfo = RsTokenInfo.builder()
                    .accessTokenParamName(accessTokenParamName)
                    .accessTokenString(accessTokenString)
                    .refreshTokenString(refreshTokenString)
                    .refreshTokenParamName(refreshTokenParamName)
                    .build();
            bodyBuilder.body(getTokenRsBody(rsTokenInfo));
        }
        return bodyBuilder.build();
    }

    protected Object getTokenRsBody(RsTokenInfo rsTokenInfo) {
        return Map.of(
                rsTokenInfo.getAccessTokenParamName(), rsTokenInfo.getAccessTokenString(),
                rsTokenInfo.getRefreshTokenParamName(), rsTokenInfo.getRefreshTokenString()
        );
    }
}
