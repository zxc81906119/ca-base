package com.redhat.cleanbase.common.exception;

import com.redhat.cleanbase.common.exception.content.GenericExceptionContent;

import java.util.Optional;

public abstract class GenericRtException extends RuntimeException implements GenericExceptionInterface<GenericRtException> {

    private final GenericExceptionContent content;

    public GenericRtException() {
        this(null, null);
    }

    public GenericRtException(String message) {
        this(message, null);
    }

    public GenericRtException(Throwable cause) {
        this(null, cause);
    }

    public GenericRtException(String message, Throwable cause) {
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
                .orElse(content.getCode().name());
    }


}
