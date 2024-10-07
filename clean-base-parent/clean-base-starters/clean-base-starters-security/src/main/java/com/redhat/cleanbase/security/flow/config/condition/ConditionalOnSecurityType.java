package com.redhat.cleanbase.security.flow.config.condition;

import com.redhat.cleanbase.security.flow.SecurityFlowType;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Conditional(OnSecurityFlowCondition.class)
public @interface ConditionalOnSecurityType {

    SecurityFlowType[] value();

}
