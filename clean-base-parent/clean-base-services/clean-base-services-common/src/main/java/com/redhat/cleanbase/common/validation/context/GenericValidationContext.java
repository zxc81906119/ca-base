package com.redhat.cleanbase.common.validation.context;

import com.redhat.cleanbase.common.spring.SelfDestroyBean;
import com.redhat.cleanbase.validation.GenericValidator;
import lombok.Getter;
import org.springframework.stereotype.Component;

public final class GenericValidationContext {
    @Getter
    private static GenericValidator validator;

    private GenericValidationContext() {
        throw new UnsupportedOperationException();
    }

    @Component
    static class InitClass extends SelfDestroyBean {
        public InitClass(GenericValidator validator) {
            GenericValidationContext.validator = validator;
        }
    }
}
