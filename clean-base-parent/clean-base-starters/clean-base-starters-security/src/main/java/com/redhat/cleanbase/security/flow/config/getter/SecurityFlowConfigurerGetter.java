package com.redhat.cleanbase.security.flow.config.getter;

import com.redhat.cleanbase.common.type.ConditionSelector;
import com.redhat.cleanbase.security.flow.SecurityFlowType;
import com.redhat.cleanbase.security.flow.config.SecurityFlowConfigurer;

import java.util.List;

public class SecurityFlowConfigurerGetter extends ConditionSelector<SecurityFlowType, SecurityFlowConfigurer> {

    public SecurityFlowConfigurerGetter(List<SecurityFlowConfigurer> securityFlowConfigurers) {
        super(securityFlowConfigurers);
    }

}
