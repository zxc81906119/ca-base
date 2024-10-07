package com.redhat.cleanbase.security.flow.jwt.exception;

public class JwtParseAuthenticationException extends JwtAuthenticationException {
    public JwtParseAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public JwtParseAuthenticationException(String message) {
        super(message);
    }
}
