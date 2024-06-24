package com.redhat.cleanbase.provider.config;

import com.redhat.cleanbase.provider.interceptor.TaskContextToRqHeaderInterceptor;
import feign.RequestInterceptor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

public class FeignClientConfigs {

    public static class TaskContextToRequestHeaderInterceptorConfig {
        @Bean
        public RequestInterceptor requestInterceptor(ApplicationContext applicationContext) {
            return new TaskContextToRqHeaderInterceptor(applicationContext);
        }
    }
}
