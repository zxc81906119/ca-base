package com.redhat.cleanbase.common.i18n.msg_source;

import com.redhat.cleanbase.common.wrapper.Wrapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public final class MessageSourceWrapper implements Wrapper<MessageSource> {

    private final MessageSource messageSource;

    public String getMessageWithLocale(@NonNull String key, Object[] args, Locale locale, String defaultValue) {
        val finalDefaultValue = Optional.ofNullable(defaultValue)
                .orElse(key);
        try {
            return messageSource.getMessage(
                    key,
                    args,
                    finalDefaultValue,
                    Optional.ofNullable(locale)
                            .orElseGet(LocaleContextHolder::getLocale)
            );
        } catch (Exception e) {
            log.error("error to find i18n message key: {}", key, e);
            return finalDefaultValue;
        }
    }

    public String getMessage(String key) {
        return getMessageWithLocale(key, null, null, null);
    }

    public String getMessage(String key, String defaultValue) {
        return getMessageWithLocale(key, null, null, defaultValue);
    }

    public String getMessage(String key, Object[] args) {
        return getMessageWithLocale(key, args, null, null);
    }

    public String getMessage(String key, Object[] args, String defaultValue) {
        return getMessageWithLocale(key, args, null, defaultValue);
    }

    @Override
    public MessageSource unwrap() {
        return messageSource;
    }


}
