package com.redhat.cleanbase.i18n.msg_source;

import com.redhat.cleanbase.i18n.msg_source.base.CustomMsgSource;
import com.redhat.cleanbase.i18n.msg_source.input.GenericI18nInput;
import com.redhat.cleanbase.i18n.msg_source.input.I18nInput;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.AbstractResourceBasedMessageSource;

import java.util.Locale;

@RequiredArgsConstructor
public class PropMsgSourceProxy implements CustomMsgSource {

    private final AbstractResourceBasedMessageSource delegate;

    @Override
    public boolean isSupported(Class<? extends I18nInput> i18nInputClazz) {
        return GenericI18nInput.class.equals(i18nInputClazz);
    }

    @Override
    public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
        return delegate.getMessage(code, args, defaultMessage, locale);
    }

    @Override
    public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
        return delegate.getMessage(code, args, locale);
    }

    @Override
    public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
        return delegate.getMessage(resolvable, locale);
    }


    @Override
    public String getMessage(I18nInput input, boolean forceDefaultNull, Locale locale) {
        val code = input.getCode();
        val arguments = input.getArguments();
        return forceDefaultNull ?
                getMessage(code, arguments, null, locale)
                : getMessage(code, arguments, input.getDefaultMessage(), locale);
    }
}
