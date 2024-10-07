package com.redhat.cleanbase.security.exception;

import com.redhat.cleanbase.exception.base.GenericException;
import com.redhat.cleanbase.exception.info.ExceptionInfo;

@ExceptionInfo
public class SecurityPropValidationException extends GenericException {

    public SecurityPropValidationException(String message) {
        super(message, null);
    }
}
