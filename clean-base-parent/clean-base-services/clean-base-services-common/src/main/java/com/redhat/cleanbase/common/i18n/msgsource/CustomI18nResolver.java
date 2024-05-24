package com.redhat.cleanbase.common.i18n.msgsource;

import com.redhat.cleanbase.common.i18n.msgsource.resolver.I18nResolver;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class CustomI18nResolver implements I18nResolver {
    @NonNull
    private final List<MessageSource> messageSourceList;

    public String getMessageWithLocale(@NonNull String key, Object[] args, Locale locale, String defaultValue) {
        val finalLocale = Optional.ofNullable(locale)
                .orElseGet(LocaleContextHolder::getLocale);
        return messageSourceList.stream()
                .map((messageSource) -> {
                    try {
                        return messageSource.getMessage(
                                key,
                                args,
                                null,
                                finalLocale);
                    } catch (Exception e) {
                        log.error("error to find i18n message key: {}", key, e);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .findFirst()
                .or(() -> Optional.ofNullable(defaultValue))
                .orElse(key);
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


}
