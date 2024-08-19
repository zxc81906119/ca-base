package com.redhat.cleanbase.common.spring;

import jakarta.annotation.PostConstruct;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

public abstract class SelfDestroyBean {
    @Autowired
    private ApplicationContext context;

    @PostConstruct
    private void destroy() {
        val beanNames = context.getBeanNamesForType(getClass());
        val registry =
                (BeanDefinitionRegistry) context.getAutowireCapableBeanFactory();
        Arrays.stream(beanNames)
                .forEach(registry::removeBeanDefinition);
    }
}
