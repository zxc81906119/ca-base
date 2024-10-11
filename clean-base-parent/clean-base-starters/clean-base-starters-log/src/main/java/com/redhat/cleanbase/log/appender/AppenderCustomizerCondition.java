package com.redhat.cleanbase.log.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.redhat.cleanbase.common.type.Condition;

public interface AppenderCustomizerCondition extends Condition<Appender<ILoggingEvent>>, AppenderCustomizer {
}