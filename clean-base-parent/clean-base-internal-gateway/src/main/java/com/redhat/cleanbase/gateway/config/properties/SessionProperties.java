package com.redhat.cleanbase.gateway.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("internal-gateway.spring.session")
public class SessionProperties {
    private String headerName = "x-auth-token";
}
