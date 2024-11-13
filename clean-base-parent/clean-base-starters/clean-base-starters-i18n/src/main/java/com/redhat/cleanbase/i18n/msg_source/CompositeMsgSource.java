package com.redhat.cleanbase.i18n.msg_source;

import com.redhat.cleanbase.i18n.msg_source.base.ConvenientMsgSource;
import com.redhat.cleanbase.i18n.msg_source.base.CustomMsgSource;
import com.redhat.cleanbase.i18n.msg_source.input.I18nInput;
import com.redhat.cleanbase.i18n.msg_source.condition.selector.CustomMsgSourceSelector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class CompositeMsgSource implements ConvenientMsgSource {

    private final CustomMsgSourceSelector customMsgSourceSelector;

    private static String getMsgOrNull(I18nInput input, Locale finalLocale, CustomMsgSource msgSource) {
        try {
            return msgSource.getMessage(input, true, finalLocale);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getMessage(I18nInput input, Locale locale) {

        val finalLocale = Optional.ofNullable(locale)
                .orElseGet(LocaleContextHolder::getLocale);

        return customMsgSourceSelector.getConditionStream(input.getClass())
                .map((msgSource) -> getMsgOrNull(input, finalLocale, msgSource))
                .filter(Objects::nonNull)
                .findFirst()
                .or(() -> Optional.ofNullable(input.getDefaultMessage()))
                .orElseGet(input::getCode);
    }
}
