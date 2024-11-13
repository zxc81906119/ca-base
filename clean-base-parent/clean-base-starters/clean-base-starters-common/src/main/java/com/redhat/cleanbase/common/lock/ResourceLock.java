package com.redhat.cleanbase.common.lock;

public interface ResourceLock {
    <O, E extends Exception> O lock(String resourceName, LockAction<O, E> lockAction) throws E;
}
