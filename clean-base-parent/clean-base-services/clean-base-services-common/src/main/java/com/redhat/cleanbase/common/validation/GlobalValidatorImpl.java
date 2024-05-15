package com.redhat.cleanbase.common.validation;

import jakarta.validation.Validator;
import org.springframework.stereotype.Component;

@Component
public record GlobalValidatorImpl(Validator validator) implements GlobalValidator {
}
