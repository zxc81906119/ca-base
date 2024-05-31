package com.redhat.cleanbase.common.i18n.msgsource;

import com.redhat.cleanbase.common.i18n.msgsource.input.I18nInput;
import org.springframework.context.MessageSource;

import java.util.Locale;

public interface CustomMsgSource extends MessageSource {
    String getMessage(I18nInput data, boolean forceDefaultNull, Locale locale);
}
