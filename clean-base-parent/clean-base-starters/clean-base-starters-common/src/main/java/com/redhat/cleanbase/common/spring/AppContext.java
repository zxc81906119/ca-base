package com.redhat.cleanbase.common.spring;

import com.redhat.cleanbase.common.utils.CastUtils;
import lombok.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public final class AppContext implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static Optional<ApplicationContext> getApplicationContext() {
        return Optional.ofNullable(applicationContext);
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        AppContext.applicationContext = applicationContext;
    }

    public static <T> Optional<T> getBean(String name) {
        return getApplicationContext()
                .map(applicationContext -> applicationContext.getBean(name))
                .map(CastUtils::cast);
    }

    public static <T> Optional<T> getBean(Class<T> clazz) {
        return getApplicationContext()
                .map(applicationContext -> applicationContext.getBean(clazz));
    }

    public static <T> Optional<T> getBean(Class<T> clazz, String name) {
        return getApplicationContext()
                .map(applicationContext -> applicationContext.getBean(name, clazz));
    }

}
