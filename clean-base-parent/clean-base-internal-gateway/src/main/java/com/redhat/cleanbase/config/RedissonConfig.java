package com.redhat.cleanbase.config;

import org.redisson.spring.starter.RedissonAutoConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {
    @Bean
    public RedissonAutoConfigurationCustomizer redissonAutoConfigurationCustomizer() {
        return (config) -> {
            // todo 自己稿
        };
    }
}
