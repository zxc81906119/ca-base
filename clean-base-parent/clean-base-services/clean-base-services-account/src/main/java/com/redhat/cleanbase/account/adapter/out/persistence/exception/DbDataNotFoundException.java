package com.redhat.cleanbase.account.adapter.out.persistence.exception;

import com.redhat.cleanbase.common.exception.base.GenericRtException;
import com.redhat.cleanbase.common.exception.info.ExceptionInfo;
import com.redhat.cleanbase.common.response.code.ResponseCodeEnum;
import lombok.experimental.StandardException;

@StandardException
@ExceptionInfo(
        code = ResponseCodeEnum.DB_DATA_NOT_FOUND,
        desc = "資料庫查詢資料不存在"
)
public class DbDataNotFoundException extends GenericRtException {
}
