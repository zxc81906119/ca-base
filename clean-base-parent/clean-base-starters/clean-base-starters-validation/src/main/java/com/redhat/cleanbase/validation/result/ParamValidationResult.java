package com.redhat.cleanbase.validation.result;

import jakarta.validation.ConstraintViolation;
import lombok.NonNull;
import lombok.val;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ParamValidationResult {

    private final Map<String, Set<ConstraintViolation<Object>>> errorMap;

    public ParamValidationResult(Set<ConstraintViolation<Object>> constraintViolations) {
        this.errorMap = groupConstraintViolation(constraintViolations);
    }

    private static Map<String, Set<ConstraintViolation<Object>>> groupConstraintViolation(Set<ConstraintViolation<Object>> constraintViolations) {
        if (CollectionUtils.isEmpty(constraintViolations)) {
            return null;
        }
        val map = new HashMap<String, Set<ConstraintViolation<Object>>>();
        for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
            val key = constraintViolation.getPropertyPath().toString();
            map.computeIfAbsent(key, k -> new HashSet<>())
                    .add(constraintViolation);
        }
        return map;
    }


    public List<String> getErrorMsgList(String fieldName) {
        val errorList = getErrorList(fieldName);

        if (errorList == null) {
            return null;
        }

        return errorList.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
    }

    public Set<ConstraintViolation<Object>> getErrorList(String fieldName) {
        if (isSuccess()) {
            return null;
        }
        return errorMap.get(fieldName);
    }

    public boolean hasError() {
        return !isSuccess();
    }

    public boolean hasError(String fieldName) {
        return !isSuccess() && errorMap.containsKey(fieldName);
    }

    public boolean isSuccess() {
        return errorMap == null || errorMap.isEmpty();
    }

    public <T extends Exception> void orThrowWithErrMsg(@NonNull Function<Map<String, List<String>>, T> exceptionGetter) throws T {
        orThrowWithResult((paramValidationResult) -> {
            val errorMsgMap = paramValidationResult.getErrorMsgMap();
            return exceptionGetter.apply(errorMsgMap);
        });
    }

    public <T extends Exception> void orThrowWithResult(@NonNull Function<ParamValidationResult, T> exceptionGetter) throws T {
        if (!isSuccess()) {
            val exception = exceptionGetter.apply(this);
            throw Objects.requireNonNull(exception, "must provide exception");
        }
    }

    public Map<String, List<String>> getErrorMsgMap() {
        if (isSuccess()) {
            return null;
        }
        return errorMap.entrySet().stream()
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                (entry) -> entry.getValue().stream()
                                        .map(ConstraintViolation::getMessage)
                                        .collect(Collectors.toList())
                        )
                );
    }

    public Map<String, Set<ConstraintViolation<Object>>> getErrorMap() {
        if (isSuccess()) {
            return null;
        }
        return new HashMap<>(errorMap);
    }

}