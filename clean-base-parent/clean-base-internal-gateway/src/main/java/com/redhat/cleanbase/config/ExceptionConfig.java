package com.redhat.cleanbase.config;

import com.redhat.cleanbase.exception.handler.DefaultExceptionHandler;
import com.redhat.cleanbase.exception.handler.DefaultRtExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExceptionConfig {

    public static final String EXCEPTION_HANDLER_BEAN_NAME = "exceptionHandler";
    public static final String RT_EXCEPTION_HANDLER_BEAN_NAME = "rtExceptionHandler";

    @ConditionalOnMissingBean(name = EXCEPTION_HANDLER_BEAN_NAME)
    @Bean(EXCEPTION_HANDLER_BEAN_NAME)
    public DefaultExceptionHandler defaultExceptionHandler() {
        return new DefaultExceptionHandler();
    }

    @ConditionalOnMissingBean(name = RT_EXCEPTION_HANDLER_BEAN_NAME)
    @Bean(RT_EXCEPTION_HANDLER_BEAN_NAME)
    public DefaultRtExceptionHandler defaultRtExceptionHandler() {
        return new DefaultRtExceptionHandler();
    }
}
