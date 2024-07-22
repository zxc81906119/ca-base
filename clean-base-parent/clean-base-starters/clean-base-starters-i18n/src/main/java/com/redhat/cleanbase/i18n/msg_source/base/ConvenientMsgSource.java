package com.redhat.cleanbase.i18n.msg_source.base;

import com.redhat.cleanbase.i18n.msg_source.input.GenericI18nInput;
import lombok.val;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;
import java.util.Optional;

public interface ConvenientMsgSource extends I18nMessageSource {

    @Override
    default String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
        return getMessage(new GenericI18nInput(code, args, defaultMessage), locale);
    }

    @Override
    default String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
        return getMessage(code, args, null, locale);
    }

    @Override
    default String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
        return getMessage(toI18nInput(resolvable), locale);
    }

    private static GenericI18nInput toI18nInput(MessageSourceResolvable resolvable) {
        val code = Optional.ofNullable(resolvable.getCodes())
                .filter((codes) -> codes.length != 0)
                .map((codes) -> codes[0])
                .orElse(null);
        return new GenericI18nInput(code, resolvable.getArguments(), resolvable.getDefaultMessage());
    }
}
