package com.redhat.cleanbase.security.flow.config;

import com.redhat.cleanbase.common.type.Condition;
import com.redhat.cleanbase.common.utils.ReflectionUtils;
import com.redhat.cleanbase.security.config.properties.SecurityConfigProperties;
import com.redhat.cleanbase.security.exception.SecurityPropValidationException;
import com.redhat.cleanbase.security.flow.SecurityFlowType;
import com.redhat.cleanbase.security.flow.config.condition.ConditionalOnSecurityType;
import lombok.val;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import java.util.Arrays;

public interface SecurityFlowConfigurer extends Condition<SecurityFlowType> {

    default boolean isSupported(SecurityFlowType inputSecurityFlowType) {
        if (inputSecurityFlowType == null) {
            return false;
        }

        val conditionalOnSecurityType = ReflectionUtils.findAnnotation(getClass(), ConditionalOnSecurityType.class);
        if (conditionalOnSecurityType == null) {
            return false;
        }

        return Arrays.asList(conditionalOnSecurityType.value())
                .contains(inputSecurityFlowType);
    }

    default void validateProperties(SecurityConfigProperties properties) throws SecurityPropValidationException {
    }

    void config(HttpSecurity security, SecurityConfigProperties properties) throws Exception;
}
