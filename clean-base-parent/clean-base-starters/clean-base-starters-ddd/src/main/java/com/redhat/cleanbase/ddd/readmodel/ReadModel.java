package com.redhat.cleanbase.ddd.readmodel;

import com.redhat.cleanbase.validation.context.GenericValidationContext;
import com.redhat.cleanbase.exception.ParamValidateFailedException;

public interface ReadModel {
    default void validate() throws Exception {
        GenericValidationContext.getValidator()
                .validate(this)
                .orThrowWithErrMsg(ParamValidateFailedException::new);
    }
}