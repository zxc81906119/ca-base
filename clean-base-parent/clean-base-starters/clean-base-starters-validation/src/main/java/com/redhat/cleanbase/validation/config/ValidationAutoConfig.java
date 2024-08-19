package com.redhat.cleanbase.validation.config;

import com.redhat.cleanbase.validation.GenericValidator;
import com.redhat.cleanbase.validation.GenericValidatorImpl;
import jakarta.validation.Validator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.redhat.cleanbase.validation")
public class ValidationAutoConfig {
    @Bean
    public GenericValidator genericValidator(Validator validator) {
        return new GenericValidatorImpl(validator);
    }
}
