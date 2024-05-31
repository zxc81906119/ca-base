package com.redhat.cleanbase.common.i18n.msgsource;

import com.redhat.cleanbase.common.i18n.msgsource.input.GenericI18nInput;
import com.redhat.cleanbase.common.i18n.msgsource.input.I18nInput;
import lombok.val;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;
import java.util.Optional;

public interface ConvenientMessageSource extends MessageSource {
    String getMessage(I18nInput input, Locale locale);

    default String getMessage(I18nInput input) {
        return getMessage(input, null);
    }

    default String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
        return getMessage(new GenericI18nInput(code, args, defaultMessage), locale);
    }

    default String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
        return getMessage(code, args, null, locale);
    }

    default String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
        return getMessage(resolvableToInput(resolvable), locale);
    }

    private static GenericI18nInput resolvableToInput(MessageSourceResolvable resolvable) {
        val code = Optional.ofNullable(resolvable.getCodes())
                .filter((codes) -> codes.length != 0)
                .map((codes) -> codes[0])
                .orElse(null);
        return new GenericI18nInput(code, resolvable.getArguments(), resolvable.getDefaultMessage());
    }
}
