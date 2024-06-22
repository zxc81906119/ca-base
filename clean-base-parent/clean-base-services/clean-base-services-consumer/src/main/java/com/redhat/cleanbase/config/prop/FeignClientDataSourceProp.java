package com.redhat.cleanbase.config.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("spring.cloud.openfeign.client.datasource")
public class FeignClientDataSourceProp {
    public Boolean isInherited = false;
}
