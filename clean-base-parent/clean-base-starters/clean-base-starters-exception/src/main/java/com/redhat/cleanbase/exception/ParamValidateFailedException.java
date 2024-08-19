package com.redhat.cleanbase.exception;


import com.redhat.cleanbase.code.response.ResponseCodeEnum;
import com.redhat.cleanbase.exception.info.ExceptionInfo;

import java.util.List;
import java.util.Map;

@ExceptionInfo(
        code = ResponseCodeEnum.PARAM_VALIDATE_FAILED,
        desc = "輸入參數驗證失敗",
        example = """
                {
                  "violations": {
                    "field1": [
                      "reason11",
                      "reason12"
                    ],
                    "field2": [
                      "reason21"
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
