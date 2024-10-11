package com.redhat.cleanbase.exception.func;

public interface CatchFunc<O> {
    O doAction(Exception e) throws Exception;
}

