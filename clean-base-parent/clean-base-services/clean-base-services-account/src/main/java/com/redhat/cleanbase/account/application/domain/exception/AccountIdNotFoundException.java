package com.redhat.cleanbase.account.application.domain.exception;

import com.redhat.cleanbase.code.response.ResponseCodeEnum;
import com.redhat.cleanbase.exception.base.GenericRtException;
import com.redhat.cleanbase.exception.info.DefaultExceptionInfoSpec;

@DefaultExceptionInfoSpec(
        code = ResponseCodeEnum.ACCOUNT_ID_NOT_FOUND,
        desc = "帳戶唯一標示不存在"
)
public class AccountIdNotFoundException extends GenericRtException {

    public AccountIdNotFoundException(String message) {
        super(message, null);
    }
}
