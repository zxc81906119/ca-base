package com.redhat.cleanbase.security.flow.jwt.validator;

import com.redhat.cleanbase.security.flow.jwt.exception.JwtValidationAuthenticationException;
import com.redhat.cleanbase.security.flow.jwt.token.JwtToken;

public interface JwtValidator<T extends JwtToken> {
    void validate(T t) throws JwtValidationAuthenticationException;
}
