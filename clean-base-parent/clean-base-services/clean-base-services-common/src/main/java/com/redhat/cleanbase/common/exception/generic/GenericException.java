package com.redhat.cleanbase.common.exception.generic;


import java.util.Optional;

public abstract class GenericException extends Exception implements GenericExceptionInterface<GenericException> {

    private final GenericExceptionContent content;

    protected GenericException(String message) {
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
