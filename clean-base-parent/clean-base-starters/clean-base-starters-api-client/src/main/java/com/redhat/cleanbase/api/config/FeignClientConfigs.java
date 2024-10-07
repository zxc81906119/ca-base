package com.redhat.cleanbase.api.config;

import com.redhat.cleanbase.api.interceptor.TaskContextToRqHeaderInterceptor;
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

    public static class TaskContextToRequestHeaderInterceptorConfig {
        @Bean
        public RequestInterceptor requestInterceptor(ApplicationContext applicationContext) {
            return new TaskContextToRqHeaderInterceptor(applicationContext);
        }
    }
}
