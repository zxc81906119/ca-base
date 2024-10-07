package com.redhat.cleanbase.validation;


import jakarta.validation.Validator;

public record DefaultGenericValidator(Validator validator) implements GenericValidator {
}
