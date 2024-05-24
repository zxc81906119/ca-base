package com.redhat.cleanbase.common.i18n.msgsource.context;

import com.redhat.cleanbase.common.i18n.msgsource.resolver.I18nResolver;
import com.redhat.cleanbase.common.spring.SelfDestroyBean;
import lombok.Getter;
import org.springframework.stereotype.Component;


public final class I18nContext {
    @Getter
    private static I18nResolver i18nResolver;

    private I18nContext() {
        throw new UnsupportedOperationException();
    }

    @Component
    public static class InitClass extends SelfDestroyBean {
        public InitClass(I18nResolver i18nResolver) {
            I18nContext.i18nResolver = i18nResolver;
        }
    }

}
