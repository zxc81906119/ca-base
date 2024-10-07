package com.redhat.cleanbase.exception.handler;

import org.springframework.http.ResponseEntity;

public interface ExceptionHandler<D, E extends Exception> {

    default boolean isSupported(D d, E e) {
        return true;
    }

    ResponseEntity<?> handle(D d, E e);
}
