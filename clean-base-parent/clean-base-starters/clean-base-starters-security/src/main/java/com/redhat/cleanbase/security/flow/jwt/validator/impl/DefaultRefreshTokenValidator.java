package com.redhat.cleanbase.security.flow.jwt.validator.impl;

import com.redhat.cleanbase.security.flow.jwt.exception.JwtValidationAuthenticationException;
import com.redhat.cleanbase.security.flow.jwt.validator.RefreshTokenValidator;
import com.redhat.cleanbase.security.flow.jwt.generator.AbstractRefreshTokenGenerator;
import com.redhat.cleanbase.security.flow.jwt.token.impl.DefaultRefreshToken;
import com.redhat.cleanbase.validation.GenericValidator;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor
public class DefaultRefreshTokenValidator implements RefreshTokenValidator<DefaultRefreshToken> {

    private final GenericValidator genericValidator;

    @Override
    public void validate(DefaultRefreshToken defaultRefreshToken) throws JwtValidationAuthenticationException {
        val payload = defaultRefreshToken.getPayload();
        if (payload == null || !payload.containsKey(AbstractRefreshTokenGenerator.IS_REFRESH_TOKEN)) {
            throw new JwtValidationAuthenticationException("not refresh token");
        }
        genericValidator.validateUntilFirstFail(defaultRefreshToken)
                .orThrowWithResult((result) ->
                        new JwtValidationAuthenticationException("refresh token 欄位驗證失敗")
                );
    }
}
