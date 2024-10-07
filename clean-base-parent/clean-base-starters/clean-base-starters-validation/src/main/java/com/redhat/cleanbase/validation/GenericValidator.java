package com.redhat.cleanbase.validation;

import com.redhat.cleanbase.common.utils.ReflectionUtils;
import com.redhat.cleanbase.validation.result.ParamValidationResult;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.NonNull;
import lombok.val;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;


public interface GenericValidator {

    Validator validator();

    // 全驗,全結果
    default ParamValidationResult validate(Object object, Class<?>... groups) {
        return validateOperation((validator) ->
                validator.validate(object, groups)
        );
    }

    // 建議類別搭配 lombok field constants 造出常數
    // 單欄位驗
    default ParamValidationResult validate(Object object, String fieldName, Class<?>... groups) {
        return validateOperation((validator) ->
                validator.validateProperty(object, fieldName, groups)
        );
    }

    // 全驗,失敗即停止,驗證順序造類別屬性由上至下
    default ParamValidationResult validateUntilFirstFail(@NonNull Object object, Class<?>... groups) {
        val objectClass = object.getClass();

        val fieldNames = getValidatedFieldNames(objectClass);

        return validateUntilFirstFail(object, fieldNames, groups);
    }

    // 建議類別搭配 lombok field constants 造出常數
    // 全驗,失敗即停止,驗證順序為方法提供之欄位(包含順序)再驗類別其他欄位
    default ParamValidationResult validateUntilFirstFailWithPartialSort(Object object, @NonNull String[] fields, Class<?>... groups) {
        val objectClass = object.getClass();
        val validatedFieldNames =
                getValidatedFieldNameStream(objectClass)
                        .toList();
        val finalSortedList = new ArrayList<>(Arrays.asList(fields));
        for (String validatedFieldName : validatedFieldNames) {
            if (!finalSortedList.contains(validatedFieldName)) {
                finalSortedList.add(validatedFieldName);
            }
        }
        val array = finalSortedList.toArray(String[]::new);
        return validateUntilFirstFail(object, array, groups);
    }


    // 建議類別搭配 lombok field constants 造出常數
    // 只驗方法提供之欄位,失敗即停止,驗證順序為方法提供之欄位(包含順序)
    default ParamValidationResult validateUntilFirstFail(Object object, @NonNull String[] fields, Class<?>... groups) {
        return validateOperation((validator) ->
                Arrays.stream(fields)
                        .map((field) -> validator.validateProperty(object, field, groups))
                        .filter((constraintViolations) -> !CollectionUtils.isEmpty(constraintViolations))
                        .findFirst()
                        .orElse(null)
        );
    }


    default ParamValidationResult validateOperation(@NonNull Function<Validator, Set<ConstraintViolation<Object>>> validatorSetFunction) {
        val validator = validator();
        val constraintViolations = validatorSetFunction.apply(validator);
        return new ParamValidationResult(constraintViolations);
    }

    private boolean hasConstraint(Field field) {
        return ReflectionUtils.findAnnotation(field, Constraint.class) != null;
    }

    private String[] getValidatedFieldNames(Class<?> objectClass) {
        return getValidatedFieldNameStream(objectClass)
                .toArray(String[]::new);
    }

    private Stream<String> getValidatedFieldNameStream(Class<?> objectClass) {
        return Arrays.stream(objectClass.getDeclaredFields())
                .filter(this::hasConstraint)
                .map(Field::getName);
    }

}
