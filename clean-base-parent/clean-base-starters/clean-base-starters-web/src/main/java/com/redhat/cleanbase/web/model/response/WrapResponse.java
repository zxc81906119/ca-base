package com.redhat.cleanbase.web.model.response;

import com.redhat.cleanbase.web.model.accessor.IDataAccessor;
import com.redhat.cleanbase.web.model.accessor.IServiceAppInfoAccessor;
import com.redhat.cleanbase.web.model.info.IServiceAppInfo;

public interface WrapResponse<D, S extends IServiceAppInfo> extends IDataAccessor<D>, IServiceAppInfoAccessor<S> {
}
