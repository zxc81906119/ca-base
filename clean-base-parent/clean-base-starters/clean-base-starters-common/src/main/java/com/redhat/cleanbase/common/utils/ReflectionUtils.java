package com.redhat.cleanbase.common.utils;

import lombok.NonNull;
import lombok.val;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

public final class ReflectionUtils {
    private ReflectionUtils() {
        throw new UnsupportedOperationException();
    }

    public static <T extends Annotation> T findAnnotation(@NonNull Class<?> clazz, Class<T> annoClass) {
        return AnnotationUtils.findAnnotation(clazz, annoClass);
    }

    public static <T extends Annotation> T findAnnotation(@NonNull Field field, Class<T> annoClass) {
        return AnnotationUtils.findAnnotation(field, annoClass);
    }

    public static <T extends Annotation> T findAnnotation(@NonNull Method method, Class<T> annoClass) {
        return AnnotationUtils.findAnnotation(method, annoClass);
    }

    public static boolean isSubClazz(Class<?> parent, Class<?> someClazz) {
        return parent.isAssignableFrom(someClazz);
    }

    public static boolean isInstance(Class<?> parent, Object someClazz) {
        return parent.isInstance(someClazz);
    }

    public static boolean canInstance(Class<?> someClass) {
        return !(isInterface(someClass) || isAbstract(someClass));
    }

    public static Map<String, Object> getAnnotationAttributes(Annotation annotation) {
        return AnnotationUtils.getAnnotationAttributes(annotation);
    }

    public static boolean isAbstract(Class<?> someClass) {
        val modifiers = someClass.getModifiers();
        return Modifier.isAbstract(modifiers);
    }

    public static boolean isInterface(Class<?> someClass) {
        return someClass.isInterface();
    }
}
