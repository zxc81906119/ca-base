package com.redhat.cleanbase.validation.result;

import jakarta.validation.ConstraintViolation;
import lombok.NonNull;
import lombok.val;

import java.util.*;
import java.util.function.Function;

public class ValidationResult {

    private final Map<String, List<String>> errorMap;

    public ValidationResult(Set<ConstraintViolation<Object>> constraintViolations) {
        this.errorMap = groupViolationMessages(constraintViolations);
    }

    public boolean isSuccess() {
        return errorMap == null || errorMap.isEmpty();
    }

    public <T extends Exception> void orThrow(@NonNull Function<Map<String, List<String>>, T> exceptionGetter) throws T {
        if (!isSuccess()) {
            val errorMap = getErrorMap();
            val exception = exceptionGetter.apply(errorMap);
            throw Objects.requireNonNull(exception, "must provide exception");
        }
    }

    public <T extends Exception> void orThrowRt(Function<Map<String, List<String>>, T> exceptionGetter) {
        try {
            orThrow(exceptionGetter);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, List<String>> getErrorMap() {
        if (errorMap == null) {
            return null;
        }
        return new HashMap<>(errorMap);
    }

    private static Map<String, List<String>> groupViolationMessages(Set<ConstraintViolation<Object>> violations) {
        if (violations == null) {
            return null;
        }
        val map = new HashMap<String, List<String>>();
        for (ConstraintViolation<Object> violation : violations) {
            val key = violation.getPropertyPath().toString();
            map.computeIfAbsent(key, k -> new ArrayList<>())
                    .add(violation.getMessage());
        }
        return map;
    }

}