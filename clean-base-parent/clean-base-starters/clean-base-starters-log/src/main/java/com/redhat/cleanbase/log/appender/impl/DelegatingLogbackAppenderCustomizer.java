package com.redhat.cleanbase.log.appender.impl;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.redhat.cleanbase.common.type.ConditionSelector;
import com.redhat.cleanbase.log.appender.LogbackAppenderCustomizer;
import com.redhat.cleanbase.log.appender.LogbackAppenderCustomizerCondition;
import com.redhat.cleanbase.log.field.CustomizedLogField;

import java.util.List;

public class DelegatingLogbackAppenderCustomizer extends ConditionSelector<Appender<ILoggingEvent>, LogbackAppenderCustomizerCondition> implements LogbackAppenderCustomizer {

    public DelegatingLogbackAppenderCustomizer(List<LogbackAppenderCustomizerCondition> conditions) {
        super(conditions);
    }

    @Override
    public void customize(Appender<ILoggingEvent> appender, List<CustomizedLogField> customizedLogFields) {
        getFirstCondition(appender)
                .ifPresent((condition) -> condition.customize(appender, customizedLogFields));
    }

}