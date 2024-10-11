package com.redhat.cleanbase.log.config;

import com.redhat.cleanbase.log.appender.AppenderCustomizerCondition;
import com.redhat.cleanbase.log.appender.DelegatingAppenderCustomizer;
import com.redhat.cleanbase.log.appender.OutputStreamAppenderCustomizer;
import com.redhat.cleanbase.log.field.CustomizedLogField;
import com.redhat.cleanbase.log.field.CustomizedLogFieldAccessor;
import com.redhat.cleanbase.log.field.CustomizedLogFieldsGetter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class LogAutoConfig {

    private final List<? extends CustomizedLogField> customizedLogFields;

    public LogAutoConfig(CustomizedLogFieldsGetter customizedLogFieldsGetter) {
        this.customizedLogFields = customizedLogFieldsGetter.get();
    }

    // logging system 不要給我拔掉
    @Bean
    public DelegatingAppenderCustomizer delegatingAppenderCustomizer(LoggingSystem loggingSystem, List<AppenderCustomizerCondition> appenderCustomizerConditions) {
        return new DelegatingAppenderCustomizer(appenderCustomizerConditions, customizedLogFields);
    }

    @Bean
    public CustomizedLogFieldAccessor<?> customizedLogFieldAccessor() {
        return new CustomizedLogFieldAccessor<>(customizedLogFields);
    }

    @Bean
    @ConditionalOnMissingBean
    public OutputStreamAppenderCustomizer outputStreamAppenderCustomizer() {
        return new OutputStreamAppenderCustomizer();
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
