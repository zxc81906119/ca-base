package com.redhat.cleanbase.gateway.filter;

import com.redhat.cleanbase.gateway.exception.ExampleException;
import io.micrometer.observation.annotation.Observed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Observed
@Slf4j
@Component
public class DeviceIdGatewayFilterFactory extends AbstractGatewayFilterFactory<DeviceIdGatewayFilterFactory.Config> {

    public DeviceIdGatewayFilterFactory(
    ) {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) ->
                Optional.ofNullable(exchange.getAttribute(ServerWebExchangeUtils.CACHED_REQUEST_BODY_ATTR))
                        .filter(String.class::isInstance)
                        .map(String.class::cast)
                        .map(this::getDeviceId)
                        .map(deviceId ->
                                Mono.just(deviceId)
                                        .flatMap(this::verifyDeviceId)
                                        .then(Mono.defer(() -> chain.filter(exchange))))
                        .orElseGet(() -> Mono.error(new ExampleException("deviceId not found")));
    }

    // todo
    public String getDeviceId(String body) {
        return null;
    }

    // todo
    //  驗證失敗 -> 回傳錯誤的 mono, 由 exception handler 處理
    //  驗證成功 -> 回傳已完成的 mono, 對我來說回傳值不重要
    public Mono<Void> verifyDeviceId(String deviceId) {
        return Mono.empty();
    }

    public static class Config {

    }
}
