package com.redhat.cleanbase.provider.adapter.out.api;

import com.redhat.cleanbase.api.client.CircuitBreakerFeignClient;
import com.redhat.cleanbase.api.data.DefaultFeignClientData;
import com.redhat.cleanbase.api.proxy.FeignClientProxy;
import com.redhat.cleanbase.provider.application.port.out.CallTestApiPort;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CallTestApiPortAdapter implements CallTestApiPort {

    private final FeignClientProxy feignClientProxy;
    private final CircuitBreakerFeignClient circuitBreakerFeignClient;

    @Override
    public String callApi() throws Exception {
        val feignClientData = new DefaultFeignClientData("feignClientData");
        val proxy = feignClientProxy.proxy(circuitBreakerFeignClient, feignClientData);
        return proxy.test83();
    }
}
