package com.redhat.cleanbase.exception.base;


import com.redhat.cleanbase.exception.content.GenericExceptionContent;
import com.redhat.cleanbase.code.response.ResponseCode;
import lombok.Getter;
import lombok.Setter;

public abstract class GenericRtException extends RuntimeException implements GenericExceptionNamespace<GenericRtException> {

    @Getter
    private final GenericExceptionContent content;

    @Getter
    @Setter
    private String selfMsg;

    public GenericRtException(String message, Throwable cause, ResponseCode responseCode) {
        super(message, cause);
        this.selfMsg = message;
        this.content = initContent(responseCode);
    }

    public GenericRtException(String message, Throwable cause) {
        this(message, cause, null);
    }

    @Override
    public String getMessage() {
        return GenericExceptionNamespace.super.getMessage();
    }
}
