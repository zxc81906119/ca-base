package com.redhat.cleanbase.web.servlet.context;

import com.redhat.cleanbase.web.model.accessor.IClientAppInfoAccessor;
import com.redhat.cleanbase.web.model.accessor.IGenericAppInfoAccessor;
import com.redhat.cleanbase.web.model.accessor.IServiceAppInfoAccessor;
import com.redhat.cleanbase.web.model.info.IClientAppInfo;
import com.redhat.cleanbase.web.model.info.IGenericAppInfo;
import com.redhat.cleanbase.web.model.info.IServiceAppInfo;

public interface IAppInfoContext<C extends IClientAppInfo, S extends IServiceAppInfo, G extends IGenericAppInfo> extends IClientAppInfoAccessor<C>, IServiceAppInfoAccessor<S>, IGenericAppInfoAccessor<G> {
}
