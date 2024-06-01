package com.redhat.cleanbase.common.i18n.msg_source;

import com.redhat.cleanbase.common.i18n.msg_source.condition.I18nProcessCondition;
import com.redhat.cleanbase.common.i18n.msg_source.input.I18nInput;

import java.util.Locale;

public interface CustomMsgSource extends I18nMessageSource, I18nProcessCondition {
    String getMessage(I18nInput data, boolean forceDefaultNull, Locale locale);

    default String getMessage(I18nInput data, Locale locale) {
        return getMessage(data, false, locale);
    }
}
