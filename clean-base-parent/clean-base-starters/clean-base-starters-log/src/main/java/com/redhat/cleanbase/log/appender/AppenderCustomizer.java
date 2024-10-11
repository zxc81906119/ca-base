package com.redhat.cleanbase.log.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.redhat.cleanbase.log.field.CustomizedLogField;

import java.util.List;

public interface AppenderCustomizer {
    void customize(Appender<ILoggingEvent> appender, List<? extends CustomizedLogField> customizedLogFields);
}
