package com.redhat.cleanbase.security.flow.jwt.validator.impl;

import com.redhat.cleanbase.security.flow.jwt.exception.JwtValidationAuthenticationException;
import com.redhat.cleanbase.security.flow.jwt.validator.AccessTokenValidator;
import com.redhat.cleanbase.security.flow.jwt.generator.AbstractRefreshTokenGenerator;
import com.redhat.cleanbase.security.flow.jwt.token.impl.DefaultAccessToken;
import com.redhat.cleanbase.validation.GenericValidator;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor
public class DefaultAccessTokenValidator implements AccessTokenValidator<DefaultAccessToken> {

    private final GenericValidator genericValidator;

    @Override
    public void validate(DefaultAccessToken defaultAccessToken) throws JwtValidationAuthenticationException {
        val payload = defaultAccessToken.getPayload();
        if (payload != null && payload.containsKey(AbstractRefreshTokenGenerator.IS_REFRESH_TOKEN)) {
            throw new JwtValidationAuthenticationException("refreshToken 無法存取資源");
        }
        genericValidator
                .validateUntilFirstFail(defaultAccessToken)
                .orThrowWithResult((result) -> new JwtValidationAuthenticationException("accessToken 欄位檢核失敗"));
    }
}
