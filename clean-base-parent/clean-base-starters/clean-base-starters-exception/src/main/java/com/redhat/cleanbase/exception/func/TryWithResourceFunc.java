package com.redhat.cleanbase.exception.func;

public interface TryWithResourceFunc<R, O> {
    O doAction(R r) throws Exception;
}