package com.redhat.cleanbase.account.application.domain.exception;

import com.redhat.cleanbase.common.exception.base.GenericRtException;
import com.redhat.cleanbase.common.exception.info.ExceptionInfo;
import com.redhat.cleanbase.common.response.code.ResponseCodeEnum;
import lombok.experimental.StandardException;

@StandardException
@ExceptionInfo(
        code = ResponseCodeEnum.ACCOUNT_ID_NOT_FOUND,
        desc = "帳戶唯一標示不存在"
)
public class AccountIdNotFoundException extends GenericRtException {
}
