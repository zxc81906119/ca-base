package com.redhat.cleanbase.common.validation.context;

import com.redhat.cleanbase.common.spring.SelfDestroyBean;
import com.redhat.cleanbase.common.validation.GlobalValidator;
import lombok.Getter;
import org.springframework.stereotype.Component;

public final class GlobalValidatorContext {

    @Getter
    private static GlobalValidator validator;

    private GlobalValidatorContext() {
        throw new UnsupportedOperationException();
    }


    @Component
    public static class InitClass extends SelfDestroyBean {
        public InitClass(GlobalValidator validator) {
            GlobalValidatorContext.validator = validator;
        }
    }

}
