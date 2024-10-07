package com.redhat.cleanbase.security.flow.jwt.validator;

import com.redhat.cleanbase.security.flow.jwt.token.RefreshToken;

public interface RefreshTokenValidator<T extends RefreshToken> extends JwtValidator<T> {
}
