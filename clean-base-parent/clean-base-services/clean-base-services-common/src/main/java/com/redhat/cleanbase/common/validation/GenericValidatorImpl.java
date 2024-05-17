package com.redhat.cleanbase.common.validation;

import jakarta.validation.Validator;
import org.springframework.stereotype.Component;

@Component
public record GenericValidatorImpl(Validator validator) implements GenericValidator {
}
