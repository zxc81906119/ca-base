package com.redhat.cleanbase.common.util;

import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;

public final class ReflectionUtils {
    private ReflectionUtils() {
        throw new UnsupportedOperationException();
    }

    public static <T extends Annotation> T findAnnotation(Class<?> clazz, Class<T> annoClass) {
        return AnnotationUtils.findAnnotation(clazz, annoClass);
    }
}
