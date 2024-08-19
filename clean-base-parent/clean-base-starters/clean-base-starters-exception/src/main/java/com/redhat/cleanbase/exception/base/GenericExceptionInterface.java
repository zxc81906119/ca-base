package com.redhat.cleanbase.exception.base;

import com.redhat.cleanbase.exception.content.GenericExceptionContent;
import com.redhat.cleanbase.exception.context.GenericExceptionContext;
import com.redhat.cleanbase.common.util.CastUtils;
import lombok.val;

public interface GenericExceptionInterface<T extends Exception> {
    GenericExceptionContent getContent();

    String getMessage();

    default T toRealType() {
        return CastUtils.cast(this);
    }

    default GenericExceptionContent initContent() {
        val statusCode = GenericExceptionContext.getExceptionInfoOrThrow(getClass())
                .code();
        return new GenericExceptionContent(statusCode);
    }
}