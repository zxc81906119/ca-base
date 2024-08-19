package com.redhat.cleanbase.account.application.domain.exception;

import com.redhat.cleanbase.code.response.ResponseCodeEnum;
import com.redhat.cleanbase.exception.GenericRtException;
import com.redhat.cleanbase.exception.info.ExceptionInfo;
import lombok.experimental.StandardException;

@StandardException
@ExceptionInfo(
        code = ResponseCodeEnum.ACCOUNT_ID_NOT_FOUND,
        desc = "帳戶唯一標示不存在"
)
public class AccountIdNotFoundException extends GenericRtException {
}
