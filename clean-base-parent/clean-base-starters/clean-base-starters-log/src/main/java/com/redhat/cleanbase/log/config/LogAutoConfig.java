package com.redhat.cleanbase.log.config;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.OutputStreamAppender;
import com.redhat.cleanbase.log.appender.impl.DelegatingLogbackAppenderCustomizer;
import com.redhat.cleanbase.log.appender.LogbackAppenderCustomizerCondition;
import com.redhat.cleanbase.log.appender.impl.OutputStreamLogbackAppenderCustomizer;
import com.redhat.cleanbase.log.factory.impl.DelegatingSlf4jFactoryCustomizer;
import com.redhat.cleanbase.log.factory.impl.LogbackFactoryCustomizerCondition;
import com.redhat.cleanbase.log.factory.Slf4jFactoryCustomizeCondition;
import com.redhat.cleanbase.log.field.CustomizedLogField;
import com.redhat.cleanbase.log.field.CustomizedLogFieldAccessor;
import com.redhat.cleanbase.log.field.CustomizedLogFieldsGetter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class LogAutoConfig {

    private final List<CustomizedLogField> customizedLogFields;

    public LogAutoConfig(CustomizedLogFieldsGetter customizedLogFieldsGetter) {
        this.customizedLogFields = customizedLogFieldsGetter.get();
    }

    // logging system 不要給我拔掉
    @Bean
    public DelegatingSlf4jFactoryCustomizer delegatingSlf4jFactoryCustomizer(
            LoggingSystem loggingSystem,
            List<Slf4jFactoryCustomizeCondition> customizers
    ) {
        return new DelegatingSlf4jFactoryCustomizer(customizers, customizedLogFields);
    }

    @ConditionalOnClass(Appender.class)
    @Bean
    public DelegatingLogbackAppenderCustomizer delegatingAppenderCustomizer(List<LogbackAppenderCustomizerCondition> conditions) {
        return new DelegatingLogbackAppenderCustomizer(conditions);
    }

    @Bean
    @ConditionalOnClass(OutputStreamAppender.class)
    @ConditionalOnMissingBean
    public OutputStreamLogbackAppenderCustomizer outputStreamAppenderCustomizer() {
        return new OutputStreamLogbackAppenderCustomizer();
    }

    @ConditionalOnClass(LoggerContext.class)
    @ConditionalOnBean(DelegatingLogbackAppenderCustomizer.class)
    @Bean
    public LogbackFactoryCustomizerCondition logbackFactoryCustomizeCondition(DelegatingLogbackAppenderCustomizer delegatingLogbackAppenderCustomizer) {
        return new LogbackFactoryCustomizerCondition(delegatingLogbackAppenderCustomizer);
    }

    @Bean
    public CustomizedLogFieldAccessor customizedLogFieldAccessor() {
        return new CustomizedLogFieldAccessor(customizedLogFields);
    }

    @Configuration
    static class InnerConfig {
        @Bean
        @ConditionalOnMissingBean
        public CustomizedLogFieldsGetter customizedLogFieldsGetter(List<CustomizedLogField> customizedLogFields) {
            return () -> customizedLogFields;
        }
    }


}
