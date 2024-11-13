package com.redhat.cleanbase.web.model.request;

import com.redhat.cleanbase.web.model.accessor.IClientAppInfoAccessor;
import com.redhat.cleanbase.web.model.accessor.IDataAccessor;
import com.redhat.cleanbase.web.model.request.paging.IReqPageInfoAccessor;
import com.redhat.cleanbase.web.model.info.IClientAppInfo;

public interface WrapRequest<D, C extends IClientAppInfo> extends IDataAccessor<D>, IClientAppInfoAccessor<C>, IReqPageInfoAccessor {
}
