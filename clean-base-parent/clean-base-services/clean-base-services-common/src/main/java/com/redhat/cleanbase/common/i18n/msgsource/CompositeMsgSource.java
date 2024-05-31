package com.redhat.cleanbase.common.i18n.msgsource;

import com.redhat.cleanbase.common.i18n.msgsource.condition.I18nProcessCondition;
import com.redhat.cleanbase.common.i18n.msgsource.input.I18nInput;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
public class CompositeMsgSource implements ConvenientMsgSource {
    @NonNull
    private final List<CustomMsgSource> msgSources;

    @Override
    public String getMessage(I18nInput input, Locale locale) {
        val inputClass = input.getClass();
        val finalLocale = Optional.ofNullable(locale)
                .orElseGet(LocaleContextHolder::getLocale);
        return msgSources.stream()
                .filter((msgSource) ->
                        !(msgSource instanceof I18nProcessCondition i18nProcessCondition) || i18nProcessCondition.isSupported(inputClass))
                .map((msgSource) -> msgSource.getMessage(input, true, finalLocale))
                .filter(Objects::nonNull)
                .findFirst()
                .or(() -> Optional.ofNullable(input.getDefaultMessage()))
                .orElseGet(input::getCode);
    }

}
