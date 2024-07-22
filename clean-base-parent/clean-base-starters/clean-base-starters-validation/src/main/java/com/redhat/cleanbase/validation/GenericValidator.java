package com.redhat.cleanbase.validation;

import com.redhat.cleanbase.validation.result.ValidationResult;
import jakarta.validation.Validator;
import lombok.NonNull;
import lombok.val;


public interface GenericValidator {

    Validator validator();

    default ValidationResult validate(@NonNull Object object, Class<?>... groups) {
        val validator = validator();

        val constraintViolations =
                groups == null
                        ? validator.validate(object)
                        : validator.validate(object, groups);

        return new ValidationResult(constraintViolations);
    }

}
