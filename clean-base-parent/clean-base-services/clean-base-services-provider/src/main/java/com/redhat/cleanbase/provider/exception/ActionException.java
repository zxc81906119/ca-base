package com.redhat.cleanbase.provider.exception;

import lombok.Getter;

@Getter
public class ActionException extends Exception {

    private final String systemId;

    public ActionException(String message) {
        this(null, message);
    }

    public ActionException(String systemId, String message) {
        super(message);
        this.systemId = systemId;
    }

}
