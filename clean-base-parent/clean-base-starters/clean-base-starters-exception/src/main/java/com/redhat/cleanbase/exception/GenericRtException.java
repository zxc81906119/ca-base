package com.redhat.cleanbase.exception;


import com.redhat.cleanbase.exception.content.GenericExceptionContent;
import lombok.Getter;

import java.util.Optional;

public abstract class GenericRtException extends RuntimeException implements GenericExceptionInterface<GenericRtException> {
    @Getter
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
    public String getMessage() {
        return Optional.ofNullable(super.getMessage())
                .orElseGet(() -> content.getCode().name());
    }


}
