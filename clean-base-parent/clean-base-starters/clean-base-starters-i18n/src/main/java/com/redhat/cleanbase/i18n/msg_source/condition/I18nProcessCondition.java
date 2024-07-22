package com.redhat.cleanbase.i18n.msg_source.condition;


import com.redhat.cleanbase.i18n.msg_source.input.I18nInput;

public interface I18nProcessCondition {
    boolean isSupported(Class<? extends I18nInput> i18nInputClazz);
}