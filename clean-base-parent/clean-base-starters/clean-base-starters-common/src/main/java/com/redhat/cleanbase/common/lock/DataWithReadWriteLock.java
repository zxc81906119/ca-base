package com.redhat.cleanbase.common.lock;

import com.redhat.cleanbase.common.lock.utils.LockUtils;
import lombok.NonNull;

import java.util.concurrent.locks.ReentrantReadWriteLock;


public class DataWithReadWriteLock<T> {

    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private T data;

    public DataWithReadWriteLock(T data) {
        this.data = data;
    }

    public DataWithReadWriteLock() {
        this(null);
    }

    public T getData() {
        return LockUtils.getWithReadLock(
                () -> data,
                this.readWriteLock
        );
    }

    public void setData(T data) {
        LockUtils.setWithWriteLock(
                () -> this.data = data,
                this.readWriteLock
        );
    }

    public void doSomething(@NonNull Runnable runnable) {
        LockUtils.setWithWriteLock(runnable, this.readWriteLock);
    }


}
