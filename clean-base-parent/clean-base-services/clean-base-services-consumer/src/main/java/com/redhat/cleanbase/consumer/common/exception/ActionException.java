package com.redhat.cleanbase.consumer.common.exception;

import com.redhat.cleanbase.exception.base.GenericException;
import com.redhat.cleanbase.exception.info.ExceptionInfo;
import lombok.Getter;

@ExceptionInfo(
        desc = "系統自定義錯誤"
)
@Getter
public class ActionException extends GenericException {

    public static final String SYSTEM_ID = "systemId";

    public ActionException(String message) {
        this(null, message);
    }

    public ActionException(String systemId, String message) {
        super(message);
        if (systemId != null) {
            getContent()
                    .putDetail(SYSTEM_ID, systemId);
        }
    }

}
