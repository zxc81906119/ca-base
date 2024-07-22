package com.redhat.cleanbase.i18n.msg_source.input;

import org.springframework.context.MessageSourceResolvable;

import java.util.Optional;

public interface I18nInput extends MessageSourceResolvable {
    @Override
    default String[] getCodes() {
        return Optional.ofNullable(getCode())
                .map((code) -> new String[]{code})
                .orElse(null);
    }

    String getCode();
}