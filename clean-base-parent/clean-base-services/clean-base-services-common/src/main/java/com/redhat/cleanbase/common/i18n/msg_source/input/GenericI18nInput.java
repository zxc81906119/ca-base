package com.redhat.cleanbase.common.i18n.msg_source.input;

public class GenericI18nInput implements I18nInput {
    private final String code;
    private final Object[] arguments;
    private final String defaultMessage;

    public GenericI18nInput(String code, Object[] arguments, String defaultMessage) {
        this.code = code;
        this.arguments = arguments;
        this.defaultMessage = defaultMessage;
    }

    public GenericI18nInput(String code) {
        this(code, null, null);
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