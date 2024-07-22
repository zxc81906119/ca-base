package com.redhat.cleanbase.common.annotation;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ComponentScan("com.redhat.cleanbase.common")
public @interface EnableCommon {

    @AliasFor(annotation = ComponentScan.class)
    boolean useDefaultFilters() default true;

    @AliasFor(annotation = ComponentScan.class)
    ComponentScan.Filter[] includeFilters() default {};

    @AliasFor(annotation = ComponentScan.class)
    ComponentScan.Filter[] excludeFilters() default {};

}
