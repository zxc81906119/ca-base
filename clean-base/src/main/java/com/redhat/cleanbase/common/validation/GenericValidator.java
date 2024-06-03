package com.redhat.cleanbase.common.validation;

import com.redhat.cleanbase.common.exception.ParamValidateFailedException;
import com.redhat.cleanbase.common.validation.result.UnitResult;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.NonNull;
import lombok.val;

import java.util.*;

public interface GenericValidator {

    Validator validator();

    default UnitResult<ParamValidateFailedException> validate(@NonNull Object object, Class<?>... groups) {
        val validator = validator();

        val constraintViolations =
                groups == null
                        ? validator.validate(object)
                        : validator.validate(object, groups);

        if (constraintViolations.isEmpty()) {
            return UnitResult.success();
        }

        val groupViolationMessages = groupViolationMessages(constraintViolations);
        return UnitResult.fail(new ParamValidateFailedException(groupViolationMessages));
    }

    default void validateOrThrow(Object object, Class<?>... groups) {
        validate(object, groups)
                .throwsOnFail();
    }

    private static Map<String, List<String>> groupViolationMessages(Set<ConstraintViolation<Object>> violations) {
        val map = new HashMap<String, List<String>>();

        for (ConstraintViolation<Object> violation : violations) {
            val key = violation.getPropertyPath().toString();
            map.computeIfAbsent(key, k -> new ArrayList<>())
                    .add(violation.getMessage());
        }

        return map;
    }
}
