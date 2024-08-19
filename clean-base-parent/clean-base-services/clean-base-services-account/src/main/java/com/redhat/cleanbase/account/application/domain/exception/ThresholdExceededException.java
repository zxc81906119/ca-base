package com.redhat.cleanbase.account.application.domain.exception;


import com.redhat.cleanbase.account.application.domain.model.MoneyVo;
import com.redhat.cleanbase.code.response.ResponseCodeEnum;
import com.redhat.cleanbase.exception.GenericRtException;
import com.redhat.cleanbase.exception.info.ExceptionInfo;

@ExceptionInfo(
        code = ResponseCodeEnum.THRESHOLD_EXCEEDED,
        desc = "轉帳金額超出上限"
)
public class ThresholdExceededException extends GenericRtException {

    public ThresholdExceededException(MoneyVo threshold, MoneyVo actual) {
        super("Maximum threshold for transferring money exceeded: tried to transfer %s but threshold is %s!".formatted(actual, threshold));
    }

}
