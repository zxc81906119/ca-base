package com.redhat.cleanbase.common.wrapper;

import lombok.experimental.FieldNameConstants;
public interface Wrapper<T> {
    T unwrap();

    @FieldNameConstants
    class Methods {
        private String unwrap;
    }
}