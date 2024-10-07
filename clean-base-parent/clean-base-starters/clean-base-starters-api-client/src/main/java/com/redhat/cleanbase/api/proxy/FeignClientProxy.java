package com.redhat.cleanbase.api.proxy;

import com.redhat.cleanbase.api.data.FeignClientData;
import com.redhat.cleanbase.api.data.source.FeignClientDataSource;
import com.redhat.cleanbase.common.utils.CastUtils;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

import java.lang.reflect.InvocationTargetException;


@RequiredArgsConstructor
public class FeignClientProxy {

    private final FeignClientDataSource feignClientDataSource;

    public <T> T proxy(T openfeignClient) {
        return proxy(openfeignClient, null);
    }

    public <T> T proxy(T openfeignClient, FeignClientData feignClientData) {
        val delegateClass = openfeignClient.getClass();
        val interfaces = delegateClass.getInterfaces();
        val enhancer = new Enhancer();
        enhancer.setInterfaces(interfaces);
        enhancer.setCallback((MethodInterceptor)
                (obj, method, args, proxy) -> {
                    try (feignClientDataSource) {
                        if (feignClientData == null) {
                            feignClientDataSource.clear();
                        } else {
                            feignClientDataSource.setData(feignClientData);
                        }
                        return method.invoke(openfeignClient, args);
                    } catch (InvocationTargetException e) {
                        throw e.getTargetException();
                    }
                });

        return CastUtils.cast(enhancer.create());
    }


}
