package com.redhat.cleanbase.config;

import com.redhat.cleanbase.config.prop.FuncSwitchConfigProp;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(FuncSwitchConfigProp.class)
@Configuration
public class FuncSwitchConfig {
}
