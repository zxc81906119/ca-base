package com.redhat.cleanbase.security.flow.jwt.lock;

public interface ResourceLock {
    <O, E extends Exception> O lock(String resourceName, LockAction<O, E> lockAction) throws E;
}
