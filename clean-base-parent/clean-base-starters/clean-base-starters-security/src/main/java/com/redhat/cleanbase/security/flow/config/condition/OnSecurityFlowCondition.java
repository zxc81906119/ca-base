package com.redhat.cleanbase.security.flow.config.condition;

import com.redhat.cleanbase.security.constants.SecurityConstants;
import com.redhat.cleanbase.security.flow.SecurityFlowType;
import lombok.val;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Arrays;

public class OnSecurityFlowCondition extends SpringBootCondition {

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        val environment = context.getEnvironment();
        val inputSecurityFlowType = environment.getProperty(SecurityConstants.SECURITY_PROP_PREFIX + ".security-flow-type", SecurityFlowType.class);
        if (inputSecurityFlowType == null) {
            return ConditionOutcome.noMatch("未配置 securityType");
        }
        val annotationAttributes = metadata.getAnnotationAttributes(ConditionalOnSecurityType.class.getName());
        if (annotationAttributes == null) {
            throw new RuntimeException("請使用 ConditionalOnSecurityType 或該組合註解 !!");
        }

        val annotationSecurityFlowTypes = (SecurityFlowType[]) annotationAttributes.get("value");
        val isMatches =
                Arrays.asList(annotationSecurityFlowTypes)
                        .contains(inputSecurityFlowType);
        if (isMatches) {
            return ConditionOutcome.match("配對");
        }
        return ConditionOutcome.noMatch("未配對");
    }
}
