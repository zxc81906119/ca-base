package com.redhat.cleanbase.api.data;

import lombok.Value;

@Value
public class DefaultFeignClientData implements FeignClientData {
    String systemId;
}
