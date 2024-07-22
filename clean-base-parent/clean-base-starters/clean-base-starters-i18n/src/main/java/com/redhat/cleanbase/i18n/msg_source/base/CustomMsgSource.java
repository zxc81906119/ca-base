package com.redhat.cleanbase.i18n.msg_source.base;

import com.redhat.cleanbase.i18n.msg_source.condition.I18nProcessCondition;
import com.redhat.cleanbase.i18n.msg_source.input.I18nInput;
import jakarta.annotation.Nullable;

import java.util.Locale;

public interface CustomMsgSource extends I18nMessageSource, I18nProcessCondition {
    @Nullable
    String getMessage(I18nInput data, boolean forceDefaultNull, Locale locale);

    @Override
    default String getMessage(I18nInput data, Locale locale) {
        return getMessage(data, false, locale);
    }
}
