package com.redhat.cleanbase.common.i18n.msg_source;

import com.redhat.cleanbase.common.i18n.msg_source.condition.I18nProcessCondition;
import com.redhat.cleanbase.common.i18n.msg_source.input.I18nInput;

import java.text.MessageFormat;
import java.util.Locale;

public class CustomDBMsgSource extends CustomAbsMsgSource {
    @Override
    protected MessageFormat resolveCode(I18nInput input, Locale locale) {
        return new MessageFormat("CustomDBMsgSource", locale);
    }

    @Override
    public boolean isSupported(Class<? extends I18nInput> i18nInputClazz) {
        return false;
    }
}
