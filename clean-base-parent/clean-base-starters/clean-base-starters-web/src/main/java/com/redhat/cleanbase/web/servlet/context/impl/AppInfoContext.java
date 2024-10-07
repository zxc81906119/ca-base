package com.redhat.cleanbase.web.servlet.context.impl;

import com.redhat.cleanbase.web.model.info.impl.ClientAppInfo;
import com.redhat.cleanbase.web.model.info.impl.GenericAppInfo;
import com.redhat.cleanbase.web.model.info.impl.ServiceAppInfo;
import com.redhat.cleanbase.web.servlet.context.IAppInfoContext;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
@Getter
@Setter
public class AppInfoContext implements IAppInfoContext<ClientAppInfo, ServiceAppInfo, GenericAppInfo> {

    private ClientAppInfo clientAppInfo;
    private ServiceAppInfo serviceAppInfo;
    private GenericAppInfo genericAppInfo;

    @Override
    public ClientAppInfo newClientAppInfo() {
        return new ClientAppInfo();
    }

    @Override
    public ServiceAppInfo newServiceAppInfo() {
        return new ServiceAppInfo();
    }

    @Override
    public GenericAppInfo newGenericAppInfo() {
        return new GenericAppInfo();
    }
}
