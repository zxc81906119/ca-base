package com.redhat.cleanbase.api.config;

import feign.Feign;
import org.springframework.beans.factory.config.BeanDefinition;
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
}
