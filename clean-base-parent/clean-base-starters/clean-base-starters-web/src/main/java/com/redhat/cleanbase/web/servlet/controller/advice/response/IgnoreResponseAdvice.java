package com.redhat.cleanbase.web.servlet.controller.advice.response;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({
        ElementType.ANNOTATION_TYPE,
        ElementType.TYPE,
        ElementType.METHOD
})
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreResponseAdvice {
}
