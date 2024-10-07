package com.redhat.cleanbase.common.type;

public interface Condition<T> {
    boolean isSupported(T t);
}
