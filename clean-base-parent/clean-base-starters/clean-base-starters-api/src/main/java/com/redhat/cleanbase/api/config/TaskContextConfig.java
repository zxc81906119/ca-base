package com.redhat.cleanbase.api.config;

import com.redhat.cleanbase.api.context.TaskContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;

@Configuration
public class TaskContextConfig {
    @RequestScope
    @Bean
    public TaskContext taskContext() {
        return new TaskContext();
    }
}
