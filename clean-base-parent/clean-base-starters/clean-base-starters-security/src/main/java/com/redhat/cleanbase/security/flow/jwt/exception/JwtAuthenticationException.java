package com.redhat.cleanbase.security.flow.jwt.exception;

import com.redhat.cleanbase.code.response.ResponseCodeEnum;
import com.redhat.cleanbase.exception.base.GenericException;
import com.redhat.cleanbase.exception.info.DefaultExceptionInfoSpec;

@DefaultExceptionInfoSpec(code = ResponseCodeEnum.JWT_AUTHENTICATION_FAILED)
public class JwtAuthenticationException extends GenericException {

    public JwtAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public JwtAuthenticationException(String message) {
        this(message, null);
    }

}
