package com.redhat.cleanbase.log.factory.impl;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.redhat.cleanbase.log.appender.impl.DelegatingLogbackAppenderCustomizer;
import com.redhat.cleanbase.log.factory.Slf4jFactoryCustomizeCondition;
import com.redhat.cleanbase.log.field.CustomizedLogField;
import lombok.val;
import org.slf4j.ILoggerFactory;

import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

public class LogbackFactoryCustomizerCondition implements Slf4jFactoryCustomizeCondition {

    private final DelegatingLogbackAppenderCustomizer delegatingLogbackAppenderCustomizer;

    public LogbackFactoryCustomizerCondition(DelegatingLogbackAppenderCustomizer delegatingLogbackAppenderCustomizer) {
        this.delegatingLogbackAppenderCustomizer = delegatingLogbackAppenderCustomizer;
    }

    @Override
    public boolean isSupported(ILoggerFactory iLoggerFactory) {
        return iLoggerFactory instanceof LoggerContext;
    }

    @Override
    public void customize(ILoggerFactory iLoggerFactory, List<CustomizedLogField> customizedLogFields) {
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
                    .forEach((appender) ->
                            delegatingLogbackAppenderCustomizer.customize(appender, customizedLogFields)
                    );
        }
    }
}
