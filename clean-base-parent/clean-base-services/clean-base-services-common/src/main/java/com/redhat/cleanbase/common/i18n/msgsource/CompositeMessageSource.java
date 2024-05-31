package com.redhat.cleanbase.common.i18n.msgsource;

import com.redhat.cleanbase.common.i18n.msgsource.condition.I18nProcessCondition;
import com.redhat.cleanbase.common.i18n.msgsource.input.I18nInput;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
public class CompositeMessageSource implements ConvenientMessageSource {
    @NonNull
    private final List<MessageSource> messageSourceList;

    @Override
    public String getMessage(@NonNull I18nInput i18nInput, Locale locale) {
        val key = i18nInput.getCode();
        val i18nArgs = i18nInput.getArguments();
        val inputClazz = i18nInput.getClass();
        val finalLocale = Optional.ofNullable(locale)
                .orElseGet(LocaleContextHolder::getLocale);
        return messageSourceList.stream()
                .filter((messageSource) -> canProcess(inputClazz, messageSource))
                .map((messageSource) -> getI18nValue(messageSource, key, i18nArgs, finalLocale))
                .filter(Objects::nonNull)
                .findFirst()
                .or(() -> Optional.ofNullable(i18nInput.getDefaultMessage()))
                .orElse(key);
    }

    private static String getI18nValue(MessageSource messageSource, String key, Object[] args, Locale locale) {
        try {
            return messageSource.getMessage(
                    key,
                    args,
                    null,
                    locale);
        } catch (Exception e) {
            log.error("error to find i18n message key: {}", key, e);
        }
        return null;
    }

    private static boolean canProcess(Class<? extends I18nInput> i18nInputClazz, MessageSource messageSource) {
        return !(messageSource instanceof I18nProcessCondition i18nProcessCondition) || i18nProcessCondition.canProcess(i18nInputClazz);
    }
}
