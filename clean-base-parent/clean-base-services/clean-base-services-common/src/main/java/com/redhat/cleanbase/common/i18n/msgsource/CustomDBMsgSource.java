package com.redhat.cleanbase.common.i18n.msgsource;

import com.redhat.cleanbase.common.i18n.msgsource.condition.I18nProcessCondition;
import com.redhat.cleanbase.common.i18n.msgsource.input.I18nInput;

import java.text.MessageFormat;
import java.util.Locale;

public class CustomDBMsgSource extends CustomAbsMsgSource implements I18nProcessCondition {
    @Override
    protected MessageFormat resolveCode(I18nInput input, Locale locale) {
        return new MessageFormat("CustomDBMsgSource", locale);
    }

    @Override
    public boolean isSupported(Class<? extends I18nInput> i18nInputClazz) {
        return false;
    }
}
