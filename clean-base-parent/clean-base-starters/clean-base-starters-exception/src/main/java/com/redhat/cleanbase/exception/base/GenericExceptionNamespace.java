package com.redhat.cleanbase.exception.base;

import com.redhat.cleanbase.code.response.ResponseCode;
import com.redhat.cleanbase.common.utils.CastUtils;
import com.redhat.cleanbase.exception.content.GenericExceptionContent;
import com.redhat.cleanbase.exception.context.GenericExceptionContext;
import com.redhat.cleanbase.exception.info.ExceptionInfo;
import com.redhat.cleanbase.i18n.msg_source.context.I18nContext;
import com.redhat.cleanbase.i18n.msg_source.input.GenericI18nInput;
import lombok.val;

import java.util.Optional;

public interface GenericExceptionNamespace<T extends Exception> {

    String ERROR_CODE_S_ERROR_MESSAGE_S = "ErrorCode: %s, ErrorMessage: %s";

    GenericExceptionContent getContent();

    String getSelfMsg();

    void setSelfMsg(String msg);

    default String getMessage() {
        val selfMsg = getSelfMsg();
        if (selfMsg != null) {
            return selfMsg;
        }
        val responseCode = getContent().getResponseCode();
        val rootCode = responseCode.getRoot();
        val i18nValue = I18nContext.getMessageSource()
                .getMessage(new GenericI18nInput(rootCode.getI18nKey(), null, rootCode.getDefaultMessage()));
        val formatMessage = ERROR_CODE_S_ERROR_MESSAGE_S.formatted(responseCode.getValue(), i18nValue);
        setSelfMsg(formatMessage);
        return formatMessage;
    }

    default T toSelf() {
        return CastUtils.cast(this);
    }

    default GenericExceptionContent initContent(ResponseCode inputResponseCode) {
        val responseCode =
                Optional.ofNullable(inputResponseCode)
                        .or(() ->
                                GenericExceptionContext.getExceptionInfoOpt(getClass())
                                        .map(ExceptionInfo::code)
                        )
                        .orElseThrow(() -> new RuntimeException("generic 例外必須提供 response code"));
        return new GenericExceptionContent(responseCode);
    }
}