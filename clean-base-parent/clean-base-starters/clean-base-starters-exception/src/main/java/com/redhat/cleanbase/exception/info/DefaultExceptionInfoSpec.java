package com.redhat.cleanbase.exception.info;


import com.redhat.cleanbase.code.response.ResponseCodeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ExceptionInfoSpec
public @interface DefaultExceptionInfoSpec {

    ResponseCodeEnum code() default ResponseCodeEnum.API_FAILED;

    // 下拉選單的 option
    String title() default "";

    // 每個 option 對應的 response 範例
    String example() default "";

    // 對這個 option 的描述
    String desc() default "";

}
