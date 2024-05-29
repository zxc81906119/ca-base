package com.redhat.cleanbase.common.i18n.msgsource.input;


import lombok.NonNull;

public class DefaultI18nInput implements I18nInput {
    private final String code;
    private final Object[] arguments;
    private final String defaultMessage;

    public DefaultI18nInput(@NonNull String code, Object[] arguments, String defaultMessage) {
        this.code = code;
        this.arguments = arguments;
        this.defaultMessage = defaultMessage;
    }

    public DefaultI18nInput(String code) {
        this(code, null, null);
    }

    public DefaultI18nInput(String code, Object[] arguments) {
        this(code, arguments, null);
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public Object[] getArguments() {
        return arguments;
    }

    @Override
    public String getDefaultMessage() {
        return defaultMessage;
    }
}