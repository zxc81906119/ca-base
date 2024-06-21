package com.redhat.cleanbase.aware;

import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Optional;

public class GlobalApplicationContextAware implements ApplicationContextAware {
    @Getter
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        GlobalApplicationContextAware.applicationContext = applicationContext;
    }

    public static Optional<ApplicationContext> getApplicationContextOpt() {
        return Optional.ofNullable(applicationContext);
    }
}
