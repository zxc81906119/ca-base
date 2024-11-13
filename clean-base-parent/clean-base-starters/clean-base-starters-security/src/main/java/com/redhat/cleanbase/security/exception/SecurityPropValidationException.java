package com.redhat.cleanbase.security.exception;

import com.redhat.cleanbase.exception.info.DefaultExceptionInfoSpec;
import com.redhat.cleanbase.exception.base.GenericException;

@DefaultExceptionInfoSpec
public class SecurityPropValidationException extends GenericException {

    public SecurityPropValidationException(String message) {
        super(message, null);
    }
}
