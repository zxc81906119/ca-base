package com.redhat.cleanbase.api.data;

import lombok.Data;

public interface FeignClientData {
    String getSystemId();

    @Data
    class Default implements FeignClientData {
        private String systemId;
    }
}