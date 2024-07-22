package com.redhat.cleanbase.i18n.msg_source.input;

public class GenericI18nInput implements I18nInput {
    private final String code;
    private final Object[] arguments;
    private final String defaultMessage;


    public GenericI18nInput(String code) {
        this(code, null, null);
    }


    public GenericI18nInput(String code, Object[] arguments) {
        this(code, arguments, null);
    }

    public GenericI18nInput(String code, Object[] arguments, String defaultMessage) {
        this.code = code;
        this.arguments = arguments;
        this.defaultMessage = defaultMessage;
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