package com.redhat.cleanbase.validation;


import jakarta.validation.Validator;

public record GenericValidatorImpl(Validator validator) implements GenericValidator {
}
