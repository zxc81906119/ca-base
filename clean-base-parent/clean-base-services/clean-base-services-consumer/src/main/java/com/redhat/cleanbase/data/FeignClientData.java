package com.redhat.cleanbase.data;

import lombok.Data;

public interface FeignClientData {
    String getSystemId();

    @Data
    class Default implements FeignClientData {
        private String systemId;
    }
}