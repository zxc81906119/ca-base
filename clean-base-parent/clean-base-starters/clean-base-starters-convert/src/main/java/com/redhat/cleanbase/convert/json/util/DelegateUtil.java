package com.redhat.cleanbase.convert.json.util;

import com.redhat.cleanbase.common.utils.CastUtils;
import com.redhat.cleanbase.convert.json.interceptor.JsonInterceptor;
import com.redhat.cleanbase.convert.json.model.base.BaseMapWrapper;
import lombok.NonNull;
import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.Enhancer;

public final class DelegateUtil {
    private DelegateUtil() {
        throw new UnsupportedOperationException();
    }

    public static <T> T getProxy(@NonNull Class<T> tClass, @NonNull Callback callback) {
        return CastUtils.cast(Enhancer.create(tClass, callback));
    }

    public static <T extends BaseMapWrapper> T getJsonModelProxy(@NonNull Class<T> tClass) {
        return getProxy(tClass, new JsonInterceptor());
    }
}