package com.redhat.cleanbase.security.flow.jwt.exception;

import com.redhat.cleanbase.exception.base.GenericException;
import com.redhat.cleanbase.exception.info.ExceptionInfo;

@ExceptionInfo
public class UsernameConvertException extends GenericException {
    public UsernameConvertException(String message) {
        super(message, null, null);
    }

    public UsernameConvertException() {
        this(null);
    }
}