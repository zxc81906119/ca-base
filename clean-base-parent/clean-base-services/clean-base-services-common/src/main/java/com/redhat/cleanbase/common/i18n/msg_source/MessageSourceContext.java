package com.redhat.cleanbase.common.i18n.msg_source;

import com.redhat.cleanbase.common.spring.SelfDestroyBean;
import lombok.Getter;
import org.springframework.stereotype.Component;


public final class MessageSourceContext {
    @Getter
    private static MessageSourceWrapper messageSource;

    private MessageSourceContext() {
        throw new UnsupportedOperationException();
    }

    @Component
    public static class InitClass extends SelfDestroyBean {
        public InitClass(MessageSourceWrapper messageSourceWrapper) {
            MessageSourceContext.messageSource = messageSourceWrapper;
        }
    }

}
