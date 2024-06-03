package com.redhat.cleanbase.common.response.code;

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

    // 此方法讓子類調用進行 cache , 不要每次都遞迴抓 root
    default ResponseCode initRoot() {
        val parent = getParent();
        return parent == null
                ? this
                : parent.initRoot();
    }

}
