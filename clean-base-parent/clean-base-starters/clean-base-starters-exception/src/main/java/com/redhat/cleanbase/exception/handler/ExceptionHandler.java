package com.redhat.cleanbase.exception.handler;

public interface ExceptionHandler<D, R, E extends Exception> {

    default boolean isSupported(D d, E e) {
        return true;
    }

    R handle(D d, E e);
}
