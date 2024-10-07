package com.redhat.cleanbase.code.response;

import com.redhat.cleanbase.code.response.enums.StatusEnum;
import lombok.val;
import org.springframework.http.HttpStatus;

public interface ResponseCode {

    String getValue();

    String getTitle();

    String getDefaultMessage();

    HttpStatus getHttpStatus();

    String name();

    default String getI18nKey() {
        return getClass().getSimpleName() + "_" + name();
    }

    default StatusEnum getStatus() {
        return StatusEnum.FAIL;
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
