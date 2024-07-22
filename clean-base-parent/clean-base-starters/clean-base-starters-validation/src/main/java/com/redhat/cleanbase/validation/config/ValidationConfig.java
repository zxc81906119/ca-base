package com.redhat.cleanbase.validation.config;

import com.redhat.cleanbase.validation.GenericValidator;
import com.redhat.cleanbase.validation.GenericValidatorImpl;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AutoConfigureAfter(ValidationAutoConfiguration.class)
@Configuration
@RequiredArgsConstructor
public class ValidationConfig {

    @Bean
    public GenericValidator genericValidator(Validator validator) {
        return new GenericValidatorImpl(validator);
    }
}
