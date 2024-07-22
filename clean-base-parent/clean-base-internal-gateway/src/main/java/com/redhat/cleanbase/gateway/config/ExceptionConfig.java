package com.redhat.cleanbase.gateway.config;

import com.redhat.cleanbase.gateway.exception.handler.ExceptionHandler;
import com.redhat.cleanbase.gateway.exception.handler.RtExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExceptionConfig {

    public static final String EXCEPTION_HANDLER_BEAN_NAME = "exceptionHandler";
    public static final String RT_EXCEPTION_HANDLER_BEAN_NAME = "rtExceptionHandler";

    @ConditionalOnMissingBean(name = EXCEPTION_HANDLER_BEAN_NAME)
    @Bean(EXCEPTION_HANDLER_BEAN_NAME)
    public ExceptionHandler defaultExceptionHandler() {
        return new ExceptionHandler();
    }

    @ConditionalOnMissingBean(name = RT_EXCEPTION_HANDLER_BEAN_NAME)
    @Bean(RT_EXCEPTION_HANDLER_BEAN_NAME)
    public RtExceptionHandler defaultRtExceptionHandler() {
        return new RtExceptionHandler();
    }
}
