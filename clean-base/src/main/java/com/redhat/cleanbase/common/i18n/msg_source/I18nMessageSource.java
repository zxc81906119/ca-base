package com.redhat.cleanbase.common.i18n.msg_source;

import com.redhat.cleanbase.common.i18n.msg_source.input.I18nInput;
import org.springframework.context.MessageSource;

import java.util.Locale;

public interface I18nMessageSource extends MessageSource {
    String getMessage(I18nInput data, Locale locale);

    default String getMessage(I18nInput data) {
        return getMessage(data, null);
    }
}
