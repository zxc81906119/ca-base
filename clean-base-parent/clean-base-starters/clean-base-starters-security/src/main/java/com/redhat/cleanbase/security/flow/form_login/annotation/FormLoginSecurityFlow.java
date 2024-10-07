package com.redhat.cleanbase.security.flow.form_login.annotation;

import com.redhat.cleanbase.security.flow.SecurityFlowType;
import com.redhat.cleanbase.security.flow.config.condition.ConditionalOnSecurityType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@ConditionalOnSecurityType(SecurityFlowType.MONOLITHIC_FORM_LOGIN)
public @interface FormLoginSecurityFlow {
}
