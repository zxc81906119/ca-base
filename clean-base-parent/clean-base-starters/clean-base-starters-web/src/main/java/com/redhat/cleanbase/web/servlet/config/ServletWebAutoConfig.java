package com.redhat.cleanbase.web.servlet.config;

import com.redhat.cleanbase.convert.parser.JacksonJsonParser;
import com.redhat.cleanbase.exception.handler.GenericExceptionFamilyHandler;
import com.redhat.cleanbase.exception.condition.ExceptionCondition;
import com.redhat.cleanbase.web.servlet.exception.handler.impl.DefaultExceptionHandler;
import com.redhat.cleanbase.web.servlet.exception.handler.impl.DefaultGenericExceptionFamilyHandler;
import com.redhat.cleanbase.web.servlet.exception.handler.impl.RqDelegateExceptionHandler;
import com.redhat.cleanbase.web.servlet.response.processor.RsEntityRsWriter;
import com.redhat.cleanbase.web.servlet.response.processor.impl.JsonRsEntityRsWriter;
import jakarta.servlet.http.HttpServletRequest;
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
    public RqDelegateExceptionHandler delegateExceptionHandler(List<ExceptionCondition<HttpServletRequest, ?>> exceptionConditionList, RsEntityRsWriter rsEntityRsWriter) {
        return new RqDelegateExceptionHandler(exceptionConditionList, rsEntityRsWriter);
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
