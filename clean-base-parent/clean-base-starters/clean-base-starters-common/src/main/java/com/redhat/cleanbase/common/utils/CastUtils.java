package com.redhat.cleanbase.common.utils;

import com.redhat.cleanbase.common.type.TypeRef;

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

    public static <T> T cast(Object o, TypeRef<T> typeRef) {
        return cast(o);
    }

}
