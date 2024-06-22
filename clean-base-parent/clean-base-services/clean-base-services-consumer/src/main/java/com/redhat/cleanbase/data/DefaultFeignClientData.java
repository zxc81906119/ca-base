package com.redhat.cleanbase.data;

import com.redhat.cleanbase.data.base.FeignClientData;
import lombok.Data;

@Data
public class DefaultFeignClientData implements FeignClientData {
    private String systemId;
}
