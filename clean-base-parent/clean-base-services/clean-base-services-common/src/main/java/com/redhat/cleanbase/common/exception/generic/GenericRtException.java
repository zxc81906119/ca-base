package com.redhat.cleanbase.common.exception.generic;

import java.util.Optional;

public abstract class GenericRtException extends RuntimeException implements GenericExceptionInterface<GenericRtException> {

    private final GenericExceptionContent content;

    protected GenericRtException(String message) {
        super(message);
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
