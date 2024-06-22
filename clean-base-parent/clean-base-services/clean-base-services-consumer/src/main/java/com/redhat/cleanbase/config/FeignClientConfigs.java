package com.redhat.cleanbase.config;

import com.redhat.cleanbase.interceptor.DefaultTaskContextToRequestHeaderInterceptor;
import feign.Feign;
import feign.RequestInterceptor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

public class FeignClientConfigs {

    public static class DefaultFeignClientBuilderConfig {
        @Bean
        @Scope(BeanDefinition.SCOPE_PROTOTYPE)
        public Feign.Builder builder() {
            return Feign.builder();
        }
    }

    public static class TaskContextToRequestHeaderConfig {
        @Bean
        public RequestInterceptor requestInterceptor(ApplicationContext applicationContext) {
            return new DefaultTaskContextToRequestHeaderInterceptor(applicationContext);
        }
    }
}
