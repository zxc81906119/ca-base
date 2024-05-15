package com.redhat.cleanbase.common.exception.context;

import com.redhat.cleanbase.common.exception.ParamValidateFailedException;
import com.redhat.cleanbase.common.exception.generic.GenericException;
import com.redhat.cleanbase.common.exception.generic.GenericRtException;
import com.redhat.cleanbase.common.exception.info.ExceptionInfo;
import com.redhat.cleanbase.common.spring.SelfDestroyBean;
import com.redhat.cleanbase.common.util.ReflectionUtil;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class GenericExceptionContext {

    private GenericExceptionContext() {
        throw new UnsupportedOperationException();
    }

    private final static List<Class<?>> GENERIC_EXCEPTIONS = new ArrayList<>();

    private final static List<Class<?>> GENERIC_RT_EXCEPTIONS = new ArrayList<>();

    @Component
    public static class InitClass extends SelfDestroyBean {
        public InitClass() throws ClassNotFoundException {
            val scanner = new ClassPathScanningCandidateComponentProvider(false);
            scanner.addIncludeFilter((metadataReader, metadataReaderFactory) ->
                    isGenericException(metadataReader)
            );
            for (BeanDefinition beanDefinition : scanner.findCandidateComponents(ParamValidateFailedException.class.getPackageName())) {
                val beanCls = Class.forName(beanDefinition.getBeanClassName());
                check(beanCls);
                if (isRtException(beanCls)) {
                    GENERIC_RT_EXCEPTIONS.add(beanCls);
                }
                GENERIC_EXCEPTIONS.add(beanCls);
            }

        }
    }

    @SneakyThrows
    private static boolean isGenericException(MetadataReader metadataReader) {
        val className = metadataReader.getClassMetadata().getClassName();
        val clazz = Class.forName(className);
        return isGenericException(clazz);
    }

    @SneakyThrows
    private static void check(Class<?> beanCls) {
        getExceptionInfoOrThrow(beanCls);
    }


    private static boolean isRtException(Class<?> beanCls) {
        return GenericRtException.class.isAssignableFrom(beanCls);
    }

    public static boolean isGenericException(Class<?> clazz) {
        return isRtException(clazz)
                || GenericException.class.isAssignableFrom(clazz);
    }


    public static ExceptionInfo getExceptionInfoOrThrow(Class<?> clazz) {
        return getExceptionInfoOpt(clazz)
                .orElseThrow(() -> new RuntimeException("exception info should not be null:" + clazz.getName()));
    }

    public static Optional<ExceptionInfo> getExceptionInfoOpt(Class<?> clazz) {
        return Optional.ofNullable(ReflectionUtil.findAnnotation(clazz, ExceptionInfo.class));
    }

    public static List<Class<?>> getGenericExceptions() {
        return new ArrayList<>(GENERIC_EXCEPTIONS);
    }

    public static List<Class<?>> getGenericRtExceptions() {
        return new ArrayList<>(GENERIC_RT_EXCEPTIONS);
    }
}
