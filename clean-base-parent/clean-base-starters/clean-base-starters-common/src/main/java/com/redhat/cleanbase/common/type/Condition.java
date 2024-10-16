package com.redhat.cleanbase.common.type;
@FunctionalInterface
public interface Condition<T> {
    boolean isSupported(T t);
}
