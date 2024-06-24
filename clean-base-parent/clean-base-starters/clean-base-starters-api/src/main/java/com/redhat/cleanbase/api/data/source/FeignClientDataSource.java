package com.redhat.cleanbase.api.data.source;

import com.redhat.cleanbase.api.data.FeignClientData;

import java.util.function.Supplier;

public interface FeignClientDataSource {
    void setData(FeignClientData t);

    void setDataIfAbsent(FeignClientData t);

    void setDataIfPresent(FeignClientData t);

    FeignClientData getDataOr(FeignClientData t);

    FeignClientData getDataOr(Supplier<FeignClientData> supplier);

    FeignClientData getData();

    void clear();
}
