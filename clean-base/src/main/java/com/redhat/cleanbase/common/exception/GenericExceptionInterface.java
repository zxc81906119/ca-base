package com.redhat.cleanbase.common.exception;

import com.redhat.cleanbase.common.exception.content.GenericExceptionContent;
import com.redhat.cleanbase.common.exception.context.GenericExceptionContext;
import com.redhat.cleanbase.common.util.CastUtil;
import lombok.val;

public interface GenericExceptionInterface<T extends Exception> {
    GenericExceptionContent getContent();

    String getMessage();

    default T toRealType() {
        return CastUtil.cast(this);
    }

    default GenericExceptionContent initContent() {
        val statusCode = GenericExceptionContext
                .getExceptionInfoOrThrow(getClass())
                .code();
        return new GenericExceptionContent(statusCode);
    }
}