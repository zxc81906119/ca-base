package com.redhat.cleanbase.common.i18n.msgsource;

import com.redhat.cleanbase.common.i18n.msgsource.condition.I18nProcessCondition;
import com.redhat.cleanbase.common.i18n.msgsource.input.GenericI18nInput;
import com.redhat.cleanbase.common.i18n.msgsource.input.I18nInput;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.AbstractResourceBasedMessageSource;

import java.util.Locale;

@RequiredArgsConstructor
public class CustomPropertiesMessageSource
        implements MessageSource, I18nProcessCondition {

    private final AbstractResourceBasedMessageSource abstractResourceBasedMessageSource;

    @Override
    public boolean canProcess(Class<? extends I18nInput> i18nInputClazz) {
        return GenericI18nInput.class.equals(i18nInputClazz);
    }

    @Override
    public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
        return abstractResourceBasedMessageSource.getMessage(code, args, defaultMessage, locale);
    }

    @Override
    public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
        return abstractResourceBasedMessageSource.getMessage(code, args, locale);
    }

    @Override
    public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
        return abstractResourceBasedMessageSource.getMessage(resolvable, locale);
    }
}
