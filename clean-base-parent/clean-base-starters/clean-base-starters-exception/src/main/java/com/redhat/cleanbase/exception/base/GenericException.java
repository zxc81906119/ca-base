package com.redhat.cleanbase.exception.base;


import com.redhat.cleanbase.code.response.ResponseCode;
import com.redhat.cleanbase.exception.content.GenericExceptionContent;
import lombok.Getter;
import lombok.Setter;

public abstract class GenericException extends Exception implements GenericExceptionNamespace<GenericException> {

    @Getter
    private final GenericExceptionContent content;

    @Getter
    @Setter
    private String selfMsg;

    public GenericException(String message, Throwable cause, ResponseCode inputResponseCode) {
        super(message, cause);
        this.selfMsg = message;
        this.content = initContent(inputResponseCode);
    }

    public GenericException(String message, Throwable cause) {
        this(message, cause, null);
    }

    @Override
    public String getMessage() {
        return GenericExceptionNamespace.super.getMessage();
    }

}
