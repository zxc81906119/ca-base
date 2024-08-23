package com.redhat.cleanbase.common.utils;

import lombok.NonNull;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public final class ReflectionUtils {
    private ReflectionUtils() {
        throw new UnsupportedOperationException();
    }

    public static <T extends Annotation> T findAnnotation(@NonNull Class<?> clazz, Class<T> annoClass) {
        return AnnotationUtils.findAnnotation(clazz, annoClass);
    }

    public static <T extends Annotation> T findAnnotation(@NonNull Method method, Class<T> annoClass) {
        return AnnotationUtils.findAnnotation(method, annoClass);
    }
}
