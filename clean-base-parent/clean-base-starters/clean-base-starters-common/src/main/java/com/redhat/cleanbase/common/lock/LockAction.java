package com.redhat.cleanbase.common.lock;

import java.util.concurrent.Callable;

@FunctionalInterface
public interface LockAction<O, E extends Exception> extends Callable<O> {
    @Override
    O call() throws E;
}
