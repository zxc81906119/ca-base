package com.redhat.cleanbase.exception.info;


import com.redhat.cleanbase.code.response.ResponseCodeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ExceptionInfo {

    ResponseCodeEnum code() default ResponseCodeEnum.API_FAILED;

    String title() default "";

    String example() default "";

    String desc() default "";

}
