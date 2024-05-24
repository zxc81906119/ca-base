package com.redhat.cleanbase.common.i18n.msgsource.resolver;

import lombok.NonNull;

import java.util.Locale;

public interface I18nResolver {
    String getMessageWithLocale(@NonNull String key, Object[] args, Locale locale, String defaultValue);

    default String getMessage(String key) {
        return getMessageWithLocale(key, null, null, null);
    }

    default String getMessage(String key, String defaultValue) {
        return getMessageWithLocale(key, null, null, defaultValue);
    }

    default String getMessage(String key, Object[] args) {
        return getMessageWithLocale(key, args, null, null);
    }

    default String getMessage(String key, Object[] args, String defaultValue) {
        return getMessageWithLocale(key, args, null, defaultValue);
    }
}
