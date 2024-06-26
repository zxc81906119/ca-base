package com.redhat.cleanbase.api.config.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("spring.cloud.openfeign.client.datasource")
public class FeignClientDataSourceProp {
    public Boolean isInherited = true;
}
