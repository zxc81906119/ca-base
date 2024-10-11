package com.redhat.cleanbase.exception.utils;

public class ExceptionUtils {
    public static void setCauseOrSuppressed(Exception newException, Exception oldException) {
        if (newException.getCause() == null) {
            newException.initCause(oldException);
        } else {
            newException.addSuppressed(oldException);
        }
    }
}
