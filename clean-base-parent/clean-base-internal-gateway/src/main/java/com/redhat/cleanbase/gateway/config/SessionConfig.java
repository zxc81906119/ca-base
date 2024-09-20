package com.redhat.cleanbase.gateway.config;

import com.redhat.cleanbase.gateway.config.properties.SessionProperties;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.session.HeaderWebSessionIdResolver;
import org.springframework.web.server.session.WebSessionIdResolver;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(SessionProperties.class)
public class SessionConfig {

    private final SessionProperties sessionProperties;
    
    @Bean
    public WebSessionIdResolver webSessionIdResolver() {
        val headerWebSessionIdResolver = new HeaderWebSessionIdResolver();
        headerWebSessionIdResolver.setHeaderName(sessionProperties.getHeaderName());
        return headerWebSessionIdResolver;
    }
}
