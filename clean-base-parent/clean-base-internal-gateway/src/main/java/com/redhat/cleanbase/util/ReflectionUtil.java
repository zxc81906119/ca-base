package com.redhat.cleanbase.util;

import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.util.Optional;

public final class ReflectionUtil {
    private ReflectionUtil() {
        throw new UnsupportedOperationException();
    }

    public static <T extends Annotation> T findAnnotation(Class<?> clazz, Class<T> annoClass) {
        return AnnotationUtils.findAnnotation(clazz, annoClass);
    }

    public static <T extends Annotation> Optional<T> findAnnotationOpt(Class<?> clazz, Class<T> annoClass) {
        return Optional.ofNullable(findAnnotation(clazz, annoClass));
    }
}
