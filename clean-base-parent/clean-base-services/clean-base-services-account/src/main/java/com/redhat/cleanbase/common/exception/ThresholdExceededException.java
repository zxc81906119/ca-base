package com.redhat.cleanbase.common.exception;


import com.redhat.cleanbase.application.domain.xxx.model.MoneyVo;
import com.redhat.cleanbase.common.exception.generic.GenericRtException;
import com.redhat.cleanbase.common.exception.info.ExceptionInfo;
import com.redhat.cleanbase.common.response.code.ResponseCodeEnum;

@ExceptionInfo(
        code = ResponseCodeEnum.THRESHOLD_EXCEEDED,
        desc = "轉帳金額超出上限"
)
public class ThresholdExceededException extends GenericRtException {

    public ThresholdExceededException(MoneyVo threshold, MoneyVo actual) {
        super("Maximum threshold for transferring money exceeded: tried to transfer %s but threshold is %s!".formatted(actual, threshold));
    }

}
