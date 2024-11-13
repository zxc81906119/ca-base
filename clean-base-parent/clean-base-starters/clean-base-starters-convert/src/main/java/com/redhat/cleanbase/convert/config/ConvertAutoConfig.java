package com.redhat.cleanbase.convert.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.cleanbase.convert.parser.JacksonJsonParser;
import com.redhat.cleanbase.convert.parser.impl.DefaultJacksonJsonParser;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConvertAutoConfig {

    @ConditionalOnClass(ObjectMapper.class)
    @Configuration
    static class Jackson {

        @ConditionalOnMissingBean(JacksonJsonParser.class)
        @Bean
        public JacksonJsonParser<?> jacksonJsonParser(ObjectProvider<ObjectMapper> provider) {
            return new DefaultJacksonJsonParser(provider.getIfUnique());
        }
    }
}
