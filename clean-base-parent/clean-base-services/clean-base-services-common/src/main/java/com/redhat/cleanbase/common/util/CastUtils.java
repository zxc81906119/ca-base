package com.redhat.cleanbase.common.util;

public final class CastUtils {
    private CastUtils() {
        throw new UnsupportedOperationException();
    }

    public static <T> T cast(Object o) {
        return (T) o;
    }

    public static <T> T cast(Object o, Class<T> tClass) {
        return tClass.isInstance(o) ?
                tClass.cast(o)
                : null;
    }

}
