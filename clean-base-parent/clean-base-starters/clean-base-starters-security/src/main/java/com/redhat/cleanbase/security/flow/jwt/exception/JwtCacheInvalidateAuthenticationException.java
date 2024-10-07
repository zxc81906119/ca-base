package com.redhat.cleanbase.security.flow.jwt.exception;

public class JwtCacheInvalidateAuthenticationException extends JwtAuthenticationException {
    public JwtCacheInvalidateAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public JwtCacheInvalidateAuthenticationException(String message) {
        this(message, null);
    }

    public JwtCacheInvalidateAuthenticationException() {
        this(null, null);
    }
}
