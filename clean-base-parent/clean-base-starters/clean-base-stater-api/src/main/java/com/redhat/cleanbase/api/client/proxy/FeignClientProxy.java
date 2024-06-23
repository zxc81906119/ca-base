package com.redhat.cleanbase.api.client.proxy;

import com.redhat.cleanbase.api.data.source.FeignClientDataSource;
import com.redhat.cleanbase.api.data.FeignClientData;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;


@RequiredArgsConstructor
public class FeignClientProxy {

    private final FeignClientDataSource feignClientDataSource;

    public <T> T proxy(T t) {
        return proxy(t, null);
    }

    public <T> T proxy(T t, FeignClientData feignClientData) {
        val enhancer = new Enhancer();
        enhancer.setSuperclass(t.getClass());
        enhancer.setCallback((MethodInterceptor)
                (obj, method, args, proxy) -> {
                    try {
                        if (feignClientData == null) {
                            feignClientDataSource.clear();
                        } else {
                            feignClientDataSource.setData(feignClientData);
                        }
                        return method.invoke(t, args);
                    } finally {
                        feignClientDataSource.clear();
                    }
                });
        return (T) enhancer.create();
    }


}
