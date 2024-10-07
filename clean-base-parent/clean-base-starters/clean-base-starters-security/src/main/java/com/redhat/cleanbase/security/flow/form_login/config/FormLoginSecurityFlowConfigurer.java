package com.redhat.cleanbase.security.flow.form_login.config;

import com.redhat.cleanbase.security.config.properties.SecurityConfigProperties;
import com.redhat.cleanbase.security.flow.config.SecurityFlowConfigurer;
import com.redhat.cleanbase.security.flow.form_login.annotation.FormLoginSecurityFlow;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;


@ConditionalOnMissingBean(FormLoginSecurityFlowConfigurer.class)
@FormLoginSecurityFlow
@Configuration
public class FormLoginSecurityFlowConfigurer implements SecurityFlowConfigurer {

    @Override
    public void config(HttpSecurity security, SecurityConfigProperties properties) throws Exception {
        security.httpBasic(AbstractHttpConfigurer::disable)
                .formLogin((configurer) -> {
                    // todo 自行實做
                });
    }

}
