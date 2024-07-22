package com.redhat.cleanbase.ftl.config;

import com.redhat.cleanbase.ftl.processor.FtlProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter(FreeMarkerAutoConfiguration.class)
@ConditionalOnBean(freemarker.template.Configuration.class)
@RequiredArgsConstructor
public class FtlConfig {

    private final freemarker.template.Configuration configuration;

    @Bean
    public FtlProcessor ftlProcessor() {
        return new FtlProcessor(configuration);
    }

}
