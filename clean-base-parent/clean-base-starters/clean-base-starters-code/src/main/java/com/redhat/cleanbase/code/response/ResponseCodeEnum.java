package com.redhat.cleanbase.code.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Optional;

@Getter
public enum ResponseCodeEnum implements ResponseCode {
    API_SUCCESS(0, HttpStatus.OK),

    API_FAILED(1),

    DB_FAILED(2001),

    DB_DATA_NOT_FOUND(2002, HttpStatus.BAD_REQUEST),

    PARAM_VALIDATE_FAILED(3001, HttpStatus.BAD_REQUEST),

    THRESHOLD_EXCEEDED(4001, HttpStatus.BAD_REQUEST),

    ACCOUNT_ID_NOT_FOUND(4002, HttpStatus.BAD_REQUEST);

    private final int value;

    private final HttpStatus httpStatus;

    private final ResponseCode parent;

    private final ResponseCode root;

    ResponseCodeEnum(int value) {
        this(value, null, null);
    }

    ResponseCodeEnum(int value, ResponseCode parent) {
        this(value, null, parent);
    }

    ResponseCodeEnum(int value, HttpStatus httpStatus) {
        this(value, httpStatus, null);
    }

    ResponseCodeEnum(int value, HttpStatus httpStatus, ResponseCode parent) {
        this.value = value;
        this.httpStatus = Optional.ofNullable(httpStatus)
                .orElse(HttpStatus.INTERNAL_SERVER_ERROR);
        this.parent = parent;
        this.root = initRoot();
    }

}
