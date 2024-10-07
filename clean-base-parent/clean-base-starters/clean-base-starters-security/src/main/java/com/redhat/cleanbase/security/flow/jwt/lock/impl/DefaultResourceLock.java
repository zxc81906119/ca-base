package com.redhat.cleanbase.security.flow.jwt.lock.impl;

import com.redhat.cleanbase.security.flow.jwt.lock.LockAction;
import com.redhat.cleanbase.security.flow.jwt.lock.ResourceLock;

import java.util.HashMap;
import java.util.Map;

public class DefaultResourceLock implements ResourceLock {

    private final Map<String, Object> lockMap = new HashMap<>();

    @Override
    public <O, E extends Exception> O lock(String resourceName, LockAction<O, E> lockAction) throws E {

        Object lockResource = lockMap.get(resourceName);
        if (lockResource == null) {
            synchronized (lockMap) {
                lockResource =
                        lockMap.computeIfAbsent(
                                resourceName,
                                k -> new Object()
                        );
            }
        }

        synchronized (lockResource) {
            return lockAction.call();
        }
    }
}
