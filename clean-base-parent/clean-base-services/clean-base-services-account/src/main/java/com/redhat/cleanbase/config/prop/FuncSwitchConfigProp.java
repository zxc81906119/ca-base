package com.redhat.cleanbase.config.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.TimeUnit;

@Data
@ConfigurationProperties(prefix = "func.switch")
public class FuncSwitchConfigProp {
    private Long defaultTtl = 30L;
    private TimeUnit defaultTtlUnit = TimeUnit.MINUTES;
    private Boolean defaultEnabled = true;
}
