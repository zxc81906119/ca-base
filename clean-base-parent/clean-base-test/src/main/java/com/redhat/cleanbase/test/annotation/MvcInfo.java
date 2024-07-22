package com.redhat.cleanbase.test.annotation;


import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MvcInfo {
    String url();

    RequestMethod httpMethod() default RequestMethod.GET;

    String requestDataPath() default "";

    String contentType() default MediaType.APPLICATION_JSON_VALUE;

    FileUploadInfo[] fileUploadInfos() default {};

}
