package com.redhat.cleanbase.log.appender;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.redhat.cleanbase.common.type.ConditionSelector;
import com.redhat.cleanbase.log.field.CustomizedLogField;
import lombok.val;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

public class DelegatingAppenderCustomizer extends ConditionSelector<Appender<ILoggingEvent>, AppenderCustomizerCondition> implements AppenderCustomizer {

    public DelegatingAppenderCustomizer(
            List<AppenderCustomizerCondition> conditions,
            List<? extends CustomizedLogField> customizedLogFields) {
        super(conditions);
        configLoggerAppender(customizedLogFields);
    }

    @Override
    public void customize(Appender<ILoggingEvent> appender, List<? extends CustomizedLogField> customizedLogFields) {
        getFirstCondition(appender)
                .ifPresent((condition) -> condition.customize(appender, customizedLogFields));
    }

    public void configLoggerAppender(List<? extends CustomizedLogField> customizedLogFields) {
        val iLoggerFactory = LoggerFactory.getILoggerFactory();
        if (iLoggerFactory instanceof LoggerContext loggerContext) {
            val loggerList = loggerContext.getLoggerList();
            loggerList.stream()
                    // 只抓 root logger 或 不繼承 parent logger 之 logger
                    .filter((logger) -> Logger.ROOT_LOGGER_NAME.equalsIgnoreCase(logger.getName()) || !logger.isAdditive())
                    // 轉成 iterator
                    .map(Logger::iteratorForAppenders)
                    // iterator 轉成 stream 攤平
                    .flatMap((appenderIterator) ->
                            StreamSupport.stream(Spliterators.spliteratorUnknownSize(appenderIterator, Spliterator.ORDERED), false)
                    )
                    // 去重複 appender
                    .distinct()
                    // 針對每個 appender 做操作
                    .forEach((appender) -> this.customize(appender, customizedLogFields));
        } else {
            throw new UnsupportedOperationException();
        }
    }


}