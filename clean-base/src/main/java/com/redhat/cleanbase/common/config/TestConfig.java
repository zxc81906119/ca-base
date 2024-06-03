package com.redhat.cleanbase.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(TestConfig.TestProperties.class)
@Configuration
public class TestConfig {

    @ConfigurationProperties("test")
    @Data
    public static class TestProperties {
        private String region;
    }

}
