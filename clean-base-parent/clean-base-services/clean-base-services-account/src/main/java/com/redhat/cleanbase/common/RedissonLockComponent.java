package com.redhat.cleanbase.common;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class RedissonLockComponent {
    private final RedissonClient redissonClient;

    public void lock(@NonNull String key, @NonNull Runnable runnable) {
        val rLock = redissonClient.getLock(key);
        rLock.lock();
        try {
            runnable.run();
        } finally {
            rLock.unlock();
        }
    }

    public void tryLock(@NonNull String key, @NonNull Runnable runnable) {
        val rLock = redissonClient.getLock(key);
        if (rLock.tryLock()) {
            try {
                runnable.run();
            } finally {
                rLock.unlock();
            }
        }
    }

    public void readLock(@NonNull String key, @NonNull Runnable runnable) {
        val lock = redissonClient.getReadWriteLock(key);
        val readLock = lock.readLock();
        readLock.lock();
        try {
            runnable.run();
        } finally {
            readLock.unlock();
        }
    }

    public void writeLock(@NonNull String key, @NonNull Runnable runnable) {
        val lock = redissonClient.getReadWriteLock(key);
        val writeLock = lock.writeLock();
        writeLock.lock();
        try {
            runnable.run();
        } finally {
            writeLock.unlock();
        }
    }

    public void tryReadLock(@NonNull String key, @NonNull Runnable runnable) {
        val lock = redissonClient.getReadWriteLock(key);
        val readLock = lock.readLock();
        if (readLock.tryLock()) {
            try {
                runnable.run();
            } finally {
                readLock.unlock();
            }
        }
    }

    public void tryWriteLock(@NonNull String key, @NonNull Runnable runnable) {
        val lock = redissonClient.getReadWriteLock(key);
        val writeLock = lock.writeLock();
        if (writeLock.tryLock()) {
            try {
                runnable.run();
            } finally {
                writeLock.unlock();
            }
        }
    }

    public void countDownLatch(@NonNull String key, @NonNull Consumer<RCountDownLatch>... countDownLatchConsumer) throws InterruptedException {
        val length = countDownLatchConsumer.length;
        if (length == 0) {
            return;
        }
        val countDownLatch = redissonClient.getCountDownLatch(key);
        if (!countDownLatch.trySetCount(length)) {
            return;
        }
        for (Consumer<RCountDownLatch> rCountDownLatchConsumer : countDownLatchConsumer) {
            rCountDownLatchConsumer.accept(countDownLatch);
        }
        countDownLatch.await();
    }

}
