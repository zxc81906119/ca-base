package com.redhat.cleanbase.security.flow.jwt.exception;

public class JwtCacheNotFoundAuthenticationException extends JwtAuthenticationException {
    public JwtCacheNotFoundAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public JwtCacheNotFoundAuthenticationException(String message) {
        this(message, null);
    }

    public JwtCacheNotFoundAuthenticationException() {
        this(null, null);
    }
}
