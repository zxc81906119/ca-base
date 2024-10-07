package com.redhat.cleanbase.web.model.accessor;

import com.redhat.cleanbase.web.model.info.IServiceAppInfo;

public interface IServiceAppInfoAccessor<S extends IServiceAppInfo> {
    S getServiceAppInfo();

    void setServiceAppInfo(S serviceAppInfo);

    S newServiceAppInfo();

    default S findServiceAppInfoOrNew() {
        S serviceAppInfo = getServiceAppInfo();
        if (serviceAppInfo == null) {
            setServiceAppInfo(serviceAppInfo = newServiceAppInfo());
        }
        return serviceAppInfo;
    }
}
