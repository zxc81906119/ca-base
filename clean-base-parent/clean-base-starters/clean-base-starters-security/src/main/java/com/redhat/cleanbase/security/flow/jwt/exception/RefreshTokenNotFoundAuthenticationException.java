package com.redhat.cleanbase.security.flow.jwt.exception;

public class RefreshTokenNotFoundAuthenticationException extends JwtAuthenticationException {

    public RefreshTokenNotFoundAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public RefreshTokenNotFoundAuthenticationException(String message) {
        this(message, null);
    }

    public RefreshTokenNotFoundAuthenticationException() {
        this(null, null);
    }


}
