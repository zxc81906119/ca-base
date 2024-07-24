package com.redhat.cleanbase.common.i18n.context;

import com.redhat.cleanbase.common.spring.SelfDestroyBean;
import com.redhat.cleanbase.i18n.msg_source.base.ConvenientMsgSource;
import com.redhat.cleanbase.i18n.msg_source.base.I18nMessageSource;
import lombok.Getter;
import org.springframework.stereotype.Component;


public final class I18nContext {
    @Getter
    private static I18nMessageSource messageSource;

    private I18nContext() {
        throw new UnsupportedOperationException();
    }

    @Component
    static class InitClass extends SelfDestroyBean {
        public InitClass(ConvenientMsgSource messageSource) {
            I18nContext.messageSource = messageSource;
        }
    }

}
