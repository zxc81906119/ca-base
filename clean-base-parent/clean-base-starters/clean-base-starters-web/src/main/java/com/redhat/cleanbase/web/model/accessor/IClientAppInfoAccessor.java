package com.redhat.cleanbase.web.model.accessor;


import com.redhat.cleanbase.web.model.info.IClientAppInfo;

public interface IClientAppInfoAccessor<C extends IClientAppInfo> {
    C getClientAppInfo();

    void setClientAppInfo(C clientAppInfo);

    C newClientAppInfo();

    default C findClientAppInfoOrNew() {
        C clientAppInfo = getClientAppInfo();
        if (clientAppInfo == null) {
            setClientAppInfo(clientAppInfo = newClientAppInfo());
        }
        return clientAppInfo;
    }

}
