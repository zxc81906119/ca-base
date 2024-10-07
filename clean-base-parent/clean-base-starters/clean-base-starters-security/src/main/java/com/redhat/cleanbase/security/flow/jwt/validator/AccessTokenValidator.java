package com.redhat.cleanbase.security.flow.jwt.validator;

import com.redhat.cleanbase.security.flow.jwt.token.AccessToken;

public interface AccessTokenValidator<T extends AccessToken> extends JwtValidator<T> {
}
