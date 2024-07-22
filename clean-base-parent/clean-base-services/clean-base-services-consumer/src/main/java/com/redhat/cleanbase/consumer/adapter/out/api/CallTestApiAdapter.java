package com.redhat.cleanbase.consumer.adapter.out.api;

import com.redhat.cleanbase.api.client.CircuitBreakerFeignClient;
import com.redhat.cleanbase.api.data.DefaultFeignClientData;
import com.redhat.cleanbase.api.proxy.FeignClientProxy;
import com.redhat.cleanbase.consumer.application.port.out.CallTestApiPort;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CallTestApiAdapter implements CallTestApiPort {

    private final FeignClientProxy feignClientProxy;
    private final CircuitBreakerFeignClient circuitBreakerFeignClient;

    @Override
    public String callApi() throws Exception {
        val feignClientData = new DefaultFeignClientData("系統代號 777");
        val proxy = feignClientProxy.proxy(circuitBreakerFeignClient, feignClientData);
        return proxy.test83();
    }
}
