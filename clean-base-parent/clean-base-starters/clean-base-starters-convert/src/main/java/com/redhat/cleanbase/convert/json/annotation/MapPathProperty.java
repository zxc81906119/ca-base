package com.redhat.cleanbase.convert.json.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ming
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MapPathProperty {
    // 借來標示用
    @AliasFor("path")
    String value() default "";

    // 如果沒有提供,用屬性名稱替代
    String path() default "";

    // 如果值為 null 就用此預設值
    String defaultValue() default "";

    // 這個是防禦defaultValue,因為預設會是""
    boolean keepNull() default true;

    // 要不要處理null值,發現是null就直接不理
    boolean processNull() default false;


}