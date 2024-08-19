package com.redhat.cleanbase.exception.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan("com.redhat.cleanbase.exception")
@Configuration
public class ExceptionAutoConfig {

    @PostConstruct
    public void init() {
        System.out.println("safsaf");
    }
}
