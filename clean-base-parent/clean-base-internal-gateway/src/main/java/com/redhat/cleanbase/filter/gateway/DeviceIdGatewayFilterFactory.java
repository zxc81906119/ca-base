package com.redhat.cleanbase.filter.gateway;

import com.redhat.cleanbase.exception.ExampleException;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Observed
@RequiredArgsConstructor
@Slf4j
@Component
public class DeviceIdGatewayFilterFactory extends AbstractGatewayFilterFactory<DeviceIdGatewayFilterFactory.Config> {

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

    public static class Config {

    }

    // todo
    public String getDeviceId(String body) {
        return null;
    }

    // todo
    public Mono<Void> verifyDeviceId(String deviceId) {
        return Mono.empty();
    }

}
