package com.redhat.cleanbase.common.validation.result;

import lombok.NonNull;

public class UnitResult<T extends Exception> {
    private final T exception;
    private final boolean isSuccess;

    private UnitResult(T exception) {
        this.exception = exception;
        this.isSuccess = exception == null;
    }

    public void throwsOnFail() throws T {
        if (!isSuccess()) {
            throw exception;
        }
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public static <E extends Exception> UnitResult<E> success() {
        return result(null);
    }

    public static <E extends Exception> UnitResult<E> fail(@NonNull E e) {
        return result(e);
    }

    public static <E extends Exception> UnitResult<E> result(E e) {
        return new UnitResult<>(e);
    }


}