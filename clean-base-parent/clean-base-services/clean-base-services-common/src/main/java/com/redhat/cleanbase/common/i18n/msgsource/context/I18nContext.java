package com.redhat.cleanbase.common.i18n.msgsource.context;

import com.redhat.cleanbase.common.i18n.msgsource.ConvenientMsgSource;
import com.redhat.cleanbase.common.spring.SelfDestroyBean;
import lombok.Getter;
import org.springframework.stereotype.Component;


public final class I18nContext {
    @Getter
    private static ConvenientMsgSource messageSource;

    private I18nContext() {
        throw new UnsupportedOperationException();
    }

    @Component
    public static class InitClass extends SelfDestroyBean {
        public InitClass(ConvenientMsgSource messageSource) {
            I18nContext.messageSource = messageSource;
        }
    }

}
