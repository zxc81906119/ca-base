package com.redhat.cleanbase.security.flow.jwt.exception;

public class JwtCacheCreateAuthenticationException extends JwtAuthenticationException {
    public JwtCacheCreateAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public JwtCacheCreateAuthenticationException(String message) {
        super(message);
    }
}
