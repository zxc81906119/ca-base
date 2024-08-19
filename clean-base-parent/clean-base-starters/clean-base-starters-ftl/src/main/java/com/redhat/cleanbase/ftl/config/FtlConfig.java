package com.redhat.cleanbase.ftl.config;

import com.redhat.cleanbase.ftl.processor.FtlProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FtlConfig {

    private final freemarker.template.Configuration configuration;

    @Bean
    public FtlProcessor ftlProcessor() {
        return new FtlProcessor(configuration);
    }

}
