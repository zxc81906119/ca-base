package com.redhat.cleanbase.api.data.source;

import com.redhat.cleanbase.api.data.FeignClientData;

import java.io.Closeable;
import java.util.Optional;
import java.util.function.Supplier;

public interface FeignClientDataSource extends Closeable {
    void setData(FeignClientData t);

    void setDataIfAbsent(FeignClientData t);

    void setDataIfPresent(FeignClientData t);

    default FeignClientData getDataOr(FeignClientData t) {
        return Optional.ofNullable(getData())
                .orElse(t);
    }

    default FeignClientData getDataOr(Supplier<FeignClientData> supplier) {
        return Optional.ofNullable(getData())
                .orElseGet(supplier);
    }

    FeignClientData getData();

    void clear();

    default void close() {
        clear();
    }
}
