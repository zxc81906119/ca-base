package com.redhat.cleanbase.web.config;

import com.redhat.cleanbase.web.reactive.config.ReactiveWebAutoConfig;
import com.redhat.cleanbase.web.servlet.config.ServletWebAutoConfig;
import com.redhat.cleanbase.web.tracing.TracerWrapper;
import io.micrometer.tracing.Tracer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.ANY)
@Configuration
public class WebAutoConfig {

    @Bean
    public TracerWrapper tracerWrapper(Tracer tracer) {
        return new TracerWrapper(tracer);
    }

    @Configuration
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @Import(ServletWebAutoConfig.class)
    static class Servlet {
    }

    @Configuration
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    @Import(ReactiveWebAutoConfig.class)
    static class Reactive {
    }

}
