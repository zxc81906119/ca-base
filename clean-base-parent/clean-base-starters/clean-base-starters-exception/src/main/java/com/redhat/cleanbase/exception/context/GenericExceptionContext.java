package com.redhat.cleanbase.exception.context;

import com.redhat.cleanbase.common.constants.GenericConstants;
import com.redhat.cleanbase.common.spring.SelfDestroyBean;
import com.redhat.cleanbase.common.utils.ReflectionUtils;
import com.redhat.cleanbase.exception.base.GenericException;
import com.redhat.cleanbase.exception.base.GenericRtException;
import com.redhat.cleanbase.exception.info.ExceptionInfo;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public final class GenericExceptionContext {

    private static final List<Class<?>> GENERIC_RT_EXCEPTIONS = new ArrayList<>();

    private GenericExceptionContext() {
        throw new UnsupportedOperationException();
    }

    public static boolean isGenericException(Class<?> clazz) {
        return isRtException(clazz) ||
                ReflectionUtils.isSubClazz(GenericException.class, clazz);
    }

    public static boolean isRtException(Class<?> beanCls) {
        return ReflectionUtils.isSubClazz(GenericRtException.class, beanCls);
    }

    public static ExceptionInfo getExceptionInfoOrThrow(Class<?> clazz) {
        return getExceptionInfoOpt(clazz)
                .orElseThrow();
    }

    public static Optional<ExceptionInfo> getExceptionInfoOpt(Class<?> clazz) {
        return Optional.ofNullable(
                ReflectionUtils.findAnnotation(clazz, ExceptionInfo.class)
        );
    }

    public static List<Class<?>> getGenericRtExceptions() {
        return new ArrayList<>(GENERIC_RT_EXCEPTIONS);
    }

    @Component
    static class InitClass extends SelfDestroyBean {

        public InitClass(@Value("${generic.exception.extra-packages:}") Set<String> extraPackages) throws ClassNotFoundException {

            val scanner = new ClassPathScanningCandidateComponentProvider(false);
            scanner.addIncludeFilter(new AssignableTypeFilter(GenericException.class));
            scanner.addIncludeFilter(new AssignableTypeFilter(GenericRtException.class));

            extraPackages.add(GenericConstants.BASE_PACKAGE_NAME);

            for (String extraPackage : extraPackages) {
                for (BeanDefinition beanDefinition : scanner.findCandidateComponents(extraPackage)) {
                    val beanCls = Class.forName(beanDefinition.getBeanClassName());
                    if (isRtException(beanCls)) {
                        GENERIC_RT_EXCEPTIONS.add(beanCls);
                    }
                }
            }

        }
    }
}
