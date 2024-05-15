package com.redhat.cleanbase.common.exception;

import com.redhat.cleanbase.common.exception.generic.GenericRtException;
import com.redhat.cleanbase.common.exception.info.ExceptionInfo;
import com.redhat.cleanbase.common.response.code.ResponseCodeEnum;

import java.util.List;
import java.util.Map;

@ExceptionInfo(
        code = ResponseCodeEnum.PARAM_VALIDATE_FAILED,
        desc = "輸入參數驗證失敗",
        example = """
                {
                    "violations": {
                        "someFiled": [
                            "reason"
                        ]
                    }
                }
                """
)
public class ParamValidateFailedException extends GenericRtException {
    public ParamValidateFailedException(Map<String, List<String>> violations) {
        this(null, violations);
    }

    public ParamValidateFailedException(String message, Map<String, List<String>> violations) {
        super(message);
        getContent().setViolations(violations);
    }
}
