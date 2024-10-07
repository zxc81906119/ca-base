package com.redhat.cleanbase.common.utils;

import lombok.NonNull;
import lombok.val;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public final class RetryUtils {

    private RetryUtils() {
        throw new UnsupportedOperationException();
    }

    public static <T> T doActionAndRetry(int retryCount, Supplier<T> supplier, Long sleepTime, Class<? extends Exception>... retryExceptions) {

        if (retryCount < 0) {
            throw new IllegalStateException("retry count must >=0 ");
        }

        List<Class<? extends Exception>> everRetryExceptionClazzList = null;

        val totalCount = retryCount + 1;
        for (int i = 0; i < totalCount; i++) {
            try {
                return supplier.get();
            } catch (Exception e) {

                if (i >= retryCount) {
                    throw e;
                }

                val throwExceptionClazz = e.getClass();
                if (everRetryExceptionClazzList != null && everRetryExceptionClazzList.contains(throwExceptionClazz)) {
                    sleepInterval(sleepTime);
                    continue;
                }

                if (isRetryable(e, retryExceptions)) {
                    if (everRetryExceptionClazzList == null) {
                        everRetryExceptionClazzList = new ArrayList<>();
                    }
                    everRetryExceptionClazzList.add(throwExceptionClazz);
                    sleepInterval(sleepTime);
                    continue;
                }

                throw e;
            }
        }

        return null;
    }

    private static void sleepInterval(Long sleepTime) {
        if (sleepTime != null) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @SafeVarargs
    private static boolean isRetryable(@NonNull Exception throwException, Class<? extends Exception>... needRetryExceptions) {
        if (needRetryExceptions == null || needRetryExceptions.length == 0) {
            // 沒指定預設要 retry
            return true;
        }
        return Arrays.stream(needRetryExceptions)
                .anyMatch((needRetryException) -> needRetryException.isInstance(throwException));
    }
}
