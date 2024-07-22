package com.redhat.cleanbase.common.exception.base;

import com.redhat.cleanbase.common.exception.content.GenericExceptionContent;
import com.redhat.cleanbase.common.exception.context.GenericExceptionContext;
import com.redhat.cleanbase.common.util.CastUtils;
import lombok.val;

public interface GenericExceptionInterface<T extends Exception> {
    GenericExceptionContent getContent();

    String getMessage();

    default T toRealType() {
        return CastUtils.cast(this);
    }

    // 提供 init content 方法給實做類調用
    default GenericExceptionContent initContent() {
        val statusCode = GenericExceptionContext.getExceptionInfoOrThrow(getClass())
                .code();
        return new GenericExceptionContent(statusCode);
    }
}