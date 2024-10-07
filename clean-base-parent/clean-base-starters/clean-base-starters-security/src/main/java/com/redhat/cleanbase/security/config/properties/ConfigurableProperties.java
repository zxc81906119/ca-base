package com.redhat.cleanbase.security.config.properties;

public interface ConfigurableProperties<T> {
    void config(T t);
}