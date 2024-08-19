package com.redhat.cleanbase.exception;


import com.redhat.cleanbase.exception.content.GenericExceptionContent;
import lombok.Getter;

import java.util.Optional;

public abstract class GenericException extends Exception implements GenericExceptionInterface<GenericException> {
    @Getter
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
    public String getMessage() {
        return Optional.ofNullable(super.getMessage())
                .orElseGet(() -> content.getCode().name());
    }

}
