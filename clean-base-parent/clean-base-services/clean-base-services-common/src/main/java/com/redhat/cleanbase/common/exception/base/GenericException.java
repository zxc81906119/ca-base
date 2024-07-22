package com.redhat.cleanbase.common.exception.base;


import com.redhat.cleanbase.common.exception.content.GenericExceptionContent;

import java.util.Optional;

public abstract class GenericException extends Exception implements GenericExceptionInterface<GenericException> {

    private final GenericExceptionContent content;

    public GenericException() {
        this(null, null);
    }

    public GenericException(String message) {
        this(message, null);
    }

    public GenericException(Throwable cause) {
        this(null, cause);
    }

    public GenericException(String message, Throwable cause) {
        super(message, cause);
        content = initContent();
    }

    @Override
    public GenericExceptionContent getContent() {
        return content;
    }

    @Override
    public String getMessage() {
        return Optional.ofNullable(super.getMessage())
                .orElseGet(() -> content.getCode().name());
    }

}
