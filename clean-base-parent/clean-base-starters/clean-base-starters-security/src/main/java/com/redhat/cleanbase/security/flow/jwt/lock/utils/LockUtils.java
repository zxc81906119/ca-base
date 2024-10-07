package com.redhat.cleanbase.security.flow.jwt.lock.utils;


import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

public final class LockUtils {
    private LockUtils() {
        throw new UnsupportedOperationException();
    }

    public static void setWithWriteLock(Runnable runnable, ReentrantReadWriteLock reentrantReadWriteLock) {
        try {
            reentrantReadWriteLock.writeLock().lock();
            runnable.run();
        } finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
    }

    public static <T> T getWithReadLock(Supplier<T> supplier, ReentrantReadWriteLock reentrantReadWriteLock) {
        try {
            reentrantReadWriteLock.readLock().lock();
            return supplier.get();
        } finally {
            reentrantReadWriteLock.readLock().unlock();
        }
    }
}
