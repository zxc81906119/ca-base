package com.redhat.cleanbase.common.spring;

import com.redhat.cleanbase.common.utils.CastUtils;
import lombok.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

@Component
public final class AppContext implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static <T> T getBean(String name) {
        return CastUtils.cast(applicationContext.getBean(name));
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static <T> T getBean(Class<T> clazz, String name) {
        return applicationContext.getBean(clazz, name);
    }

    public static <T> T getBeanOrNull(Class<T> clazz, String name) {
        return getOutputOrNull(() -> getBean(clazz, name));
    }

    public static <T> T getBeanOrNull(String name) {
        return getOutputOrNull(() -> getBean(name));
    }

    public static <T> T getBeanOrNull(Class<T> clazz) {
        return getOutputOrNull(() -> getBean(clazz));
    }

    public static <T> T getOutputOrNull(Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        AppContext.applicationContext = applicationContext;
    }

}
