package com.redhat.cleanbase.code.response;

import com.redhat.cleanbase.code.response.enums.StatusEnum;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Optional;

@Getter
public enum ResponseCodeEnum implements ResponseCode {
    API_SUCCESS("0", HttpStatus.OK, StatusEnum.SUCCESS),

    API_FAILED("1"),

    DB_FAILED("2001"),

    DB_DATA_NOT_FOUND("2002", HttpStatus.BAD_REQUEST),

    PARAM_VALIDATE_FAILED("3001", HttpStatus.BAD_REQUEST),

    THRESHOLD_EXCEEDED("4001", HttpStatus.BAD_REQUEST),

    ACCOUNT_ID_NOT_FOUND("4002", HttpStatus.BAD_REQUEST),

    JWT_AUTHENTICATION_FAILED("5001", HttpStatus.UNAUTHORIZED);

    private final String value;

    private final String defaultMessage;

    private final HttpStatus httpStatus;

    private final ResponseCode parent;

    private final ResponseCode root;

    private final StatusEnum status;

    ResponseCodeEnum(String value) {
        this(value, null, null, null, StatusEnum.FAIL);
    }

    ResponseCodeEnum(String value, HttpStatus httpStatus) {
        this(value, httpStatus, null, null, StatusEnum.FAIL);
    }

    ResponseCodeEnum(String value, ResponseCode parent) {
        this(value, null, parent, null, StatusEnum.FAIL);
    }

    ResponseCodeEnum(String value, HttpStatus httpStatus, StatusEnum status) {
        this(value, httpStatus, null, null, status);
    }

    ResponseCodeEnum(String value, HttpStatus httpStatus, ResponseCode parent, String defaultMessage, StatusEnum status) {
        this.value = value;
        this.status = status;
        this.defaultMessage = defaultMessage;
        this.httpStatus = Optional.ofNullable(httpStatus)
                .orElse(HttpStatus.INTERNAL_SERVER_ERROR);
        this.parent = parent;
        this.root = initRoot();
    }

    @Override
    public String getTitle() {
        return name();
    }
}
