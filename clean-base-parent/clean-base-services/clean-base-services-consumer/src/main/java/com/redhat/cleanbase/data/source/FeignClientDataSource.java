package com.redhat.cleanbase.data.source;


import com.redhat.cleanbase.data.base.FeignClientData;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class FeignClientDataSource {

    private final boolean isInherit;

    private final ThreadLocal<FeignClientData> threadLocal = new ThreadLocal<>();
    private final ThreadLocal<FeignClientData> inheritableThreadLocal = new InheritableThreadLocal<>();

    private final Consumer<FeignClientData> setDataFunc = (t) -> {
        if (isInherit) {
            inheritableThreadLocal.set(t);
            threadLocal.remove();
        } else {
            threadLocal.set(t);
            inheritableThreadLocal.remove();
        }
    };

    private final Supplier<FeignClientData> getDataFunc = () -> {
        if (isInherit) {
            return inheritableThreadLocal.get();
        }
        return threadLocal.get();
    };

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

    public FeignClientData getDataOr(FeignClientData t) {
        return Optional.ofNullable(getData())
                .orElse(t);
    }

    public FeignClientData getDataOr(Supplier<FeignClientData> supplier) {
        return Optional.ofNullable(getData())
                .orElseGet(supplier);
    }

    public FeignClientData getData() {
        return getDataFunc.get();
    }

    public void clear() {
        threadLocal.remove();
        inheritableThreadLocal.remove();
    }
}
