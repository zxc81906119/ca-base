package com.redhat.cleanbase.code.response;

import lombok.val;
import org.springframework.http.HttpStatus;

public interface ResponseCode {

    int getValue();

    HttpStatus getHttpStatus();

    String name();

    default String getI18nKey() {
        return name();
    }

    ResponseCode getParent();

    default ResponseCode getRoot() {
        return initRoot();
    }

    default ResponseCode initRoot() {
        val parent = getParent();
        return parent == null
                ? this
                : parent.initRoot();
    }

}
