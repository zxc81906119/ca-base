package com.redhat.cleanbase.exception.context;

import com.redhat.cleanbase.code.response.ResponseCode;
import com.redhat.cleanbase.common.constants.GenericConstants;
import com.redhat.cleanbase.common.spring.SelfDestroyBean;
import com.redhat.cleanbase.common.utils.CastUtils;
import com.redhat.cleanbase.common.utils.ReflectionUtils;
import com.redhat.cleanbase.exception.base.GenericException;
import com.redhat.cleanbase.exception.base.GenericRtException;
import com.redhat.cleanbase.exception.info.ExceptionInfo;
import com.redhat.cleanbase.exception.info.ExceptionInfoSpec;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.*;

public final class GenericExceptionContext {

    public static final String CODE = "code";
    public static final String TITLE = "title";
    public static final String EXAMPLE = "example";
    public static final String DESC = "desc";

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
        return Optional.ofNullable(getExceptionInfoByClazz(clazz))
                .map(ReflectionUtils::getAnnotationAttributes)
                .map(GenericExceptionContext::getExceptionInfoRecord);
    }

    private static ExceptionInfo getExceptionInfoRecord(Map<String, Object> annotationAttributes) {
        val code = CastUtils.cast(annotationAttributes.get(CODE), ResponseCode.class);
        val title = CastUtils.cast(annotationAttributes.get(TITLE), String.class);
        val example = CastUtils.cast(annotationAttributes.get(EXAMPLE), String.class);
        val desc = CastUtils.cast(annotationAttributes.get(DESC), String.class);
        return new ExceptionInfo(Objects.requireNonNull(code, "exception info annotation 必須存在 code method"), title, example, desc);
    }

    public static List<Class<?>> getGenericRtExceptions() {
        return new ArrayList<>(GENERIC_RT_EXCEPTIONS);
    }

    private static boolean isJdkAnnotation(Class<?> clazz) {
        val pakkage = clazz.getPackage();
        return pakkage != null && (pakkage.getName().startsWith("java.") || pakkage.getName().startsWith("jdk.internal"));
    }

    // 遞迴搜尋
    public static Annotation getExceptionInfoByClazz(Class<?> clazz) {
        if (clazz == null
                || clazz.isAnnotation()
                || isJdkAnnotation(clazz)
        ) {
            return null;
        }
        val annotation =
                getExceptionInfoByAnnotations(clazz.getAnnotations());
        if (annotation != null) {
            return annotation;
        }
        return getExceptionInfoByClazz(clazz.getSuperclass());
    }

    // 廣度搜尋法
    public static Annotation getExceptionInfoByAnnotations(Annotation[] annotations) {
        if (annotations == null || annotations.length == 0) {
            return null;
        }
        val linkedList = new LinkedList<Annotation>();
        for (Annotation annotation : annotations) {
            if (!isJdkAnnotation(annotation.annotationType())) {
                linkedList.add(annotation);
            }
        }
        if (linkedList.isEmpty()) {
            return null;
        }
        while (!linkedList.isEmpty()) {
            val pop = linkedList.pop();
            val clazz = pop.annotationType();
            if (isExceptionInfo(clazz)) {
                return pop;
            }
            for (Annotation annotation : clazz.getAnnotations()) {
                if (!isJdkAnnotation(annotation.annotationType())) {
                    linkedList.add(annotation);
                }
            }
        }
        return null;
    }

    public static boolean isExceptionInfo(Class<? extends Annotation> clazz) {
        return clazz.getAnnotation(ExceptionInfoSpec.class) != null;
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
