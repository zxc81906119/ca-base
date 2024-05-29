package com.redhat.cleanbase.common.i18n.msgsource;

import com.redhat.cleanbase.common.i18n.msgsource.input.I18nInput;
import org.springframework.context.MessageSource;

import java.util.Locale;

public interface ConvenientMessageSource extends MessageSource {
    String getMessage(I18nInput input, Locale locale);

    default String getMessage(I18nInput input) {
        return getMessage(input, null);
    }
}
