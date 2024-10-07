package com.redhat.cleanbase.exception;


import com.redhat.cleanbase.code.response.ResponseCodeEnum;
import com.redhat.cleanbase.exception.base.GenericRtException;
import com.redhat.cleanbase.exception.info.ExceptionInfo;

import java.util.List;
import java.util.Map;

// todo request 要用 common request ??
// todo 解決 code 是 enum 的問題,
// todo 組合註解　，　只要組合　ｅｘｃｅｐｔｉｏｎ　ｉｎｆｏ即可
// todo code desc example , code 是 response code 的子類
// todo global exception
// todo 轉換器 去抓 bean
// todo 提供 200 風格選擇器
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
        super(message, null, null);
        getContent().setViolations(violations);
    }

}
