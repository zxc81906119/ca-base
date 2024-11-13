package com.redhat.cleanbase.web.servlet.config;

import com.redhat.cleanbase.convert.parser.JacksonJsonParser;
import com.redhat.cleanbase.exception.handler.GenericExceptionFamilyHandler;
import com.redhat.cleanbase.web.servlet.exception.condition.RqExceptionCondition;
import com.redhat.cleanbase.web.servlet.exception.handler.DefaultExceptionHandler;
import com.redhat.cleanbase.web.servlet.exception.handler.DefaultGenericExceptionFamilyHandler;
import com.redhat.cleanbase.web.servlet.exception.handler.RqDelegatingExceptionHandler;
import com.redhat.cleanbase.web.servlet.response.processor.RsEntityRsWriter;
import com.redhat.cleanbase.web.servlet.response.processor.impl.JsonRsEntityRsWriter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
@ComponentScan("com.redhat.cleanbase.web.servlet")
public class ServletWebAutoConfig {

    @ConditionalOnMissingBean
    @Bean
    public RsEntityRsWriter writeRsEntityToRsProcessor(JacksonJsonParser<?> jacksonJsonParser) {
        return new JsonRsEntityRsWriter(jacksonJsonParser);
    }

    @Bean
    public RqDelegatingExceptionHandler delegateExceptionHandler(
            List<RqExceptionCondition<?>> exceptionConditionList,
            RsEntityRsWriter rsEntityRsWriter
    ) {
        return new RqDelegatingExceptionHandler(exceptionConditionList, rsEntityRsWriter);
    }

    @ConditionalOnMissingBean
    @Bean
    public DefaultExceptionHandler exceptionHandler() {
        return new DefaultExceptionHandler();
    }

    @ConditionalOnMissingBean
    @Bean
    public GenericExceptionFamilyHandler genericExceptionFamilyHandler() {
        return new DefaultGenericExceptionFamilyHandler();
    }

}
