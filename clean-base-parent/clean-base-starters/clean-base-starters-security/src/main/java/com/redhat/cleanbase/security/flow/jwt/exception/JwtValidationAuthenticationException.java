package com.redhat.cleanbase.security.flow.jwt.exception;

public class JwtValidationAuthenticationException extends JwtAuthenticationException {
    public JwtValidationAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public JwtValidationAuthenticationException(String message) {
        super(message);
    }
}
