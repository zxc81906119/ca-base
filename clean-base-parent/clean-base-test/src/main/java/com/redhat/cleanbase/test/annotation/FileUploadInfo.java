package com.redhat.cleanbase.test.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FileUploadInfo {

    String requestFieldName() default "ufile";

    String contentType();

    String fileName() default "";

    String filePath();
}
