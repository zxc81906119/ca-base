package com.redhat.cleanbase.api.data.source;


import com.redhat.cleanbase.api.data.FeignClientData;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DefaultFeignClientDataSource implements FeignClientDataSource {

    private final ThreadLocal<FeignClientData> threadLocal = new ThreadLocal<>();
    private final ThreadLocal<FeignClientData> inheritableThreadLocal = new InheritableThreadLocal<>();

    private final Consumer<FeignClientData> setDataFunc;
    private final Supplier<FeignClientData> getDataFunc;

    public DefaultFeignClientDataSource(boolean isInherit) {
        this.getDataFunc = () -> {
            if (isInherit) {
                return inheritableThreadLocal.get();
            }
            return threadLocal.get();
        };

        this.setDataFunc = (t) -> {
            if (isInherit) {
                inheritableThreadLocal.set(t);
                threadLocal.remove();
            } else {
                threadLocal.set(t);
                inheritableThreadLocal.remove();
            }
        };
    }

    public void setData(FeignClientData t) {
        setDataFunc.accept(t);
    }

    public void setDataIfAbsent(FeignClientData t) {
        if (getData() == null) {
            setData(t);
        }
    }

    public void setDataIfPresent(FeignClientData t) {
        Optional.ofNullable(getData())
                .ifPresent((noUse) -> setData(t));
    }

    public FeignClientData getData() {
        return getDataFunc.get();
    }

    public void clear() {
        threadLocal.remove();
        inheritableThreadLocal.remove();
    }
}
