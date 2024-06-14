package com.redhat.cleanbase.filter;

import com.redhat.cleanbase.constant.OrderConstants;
import com.redhat.cleanbase.exception.ExampleException;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Observed
@RequiredArgsConstructor
@Slf4j
@Component
@Order(OrderConstants.DEVICE_ID_FILTER_ORDER)
public class DeviceIdFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return Optional.ofNullable(exchange.getAttribute(ServerWebExchangeUtils.CACHED_REQUEST_BODY_ATTR))
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
    public Mono<Void> verifyDeviceId(String deviceId) {
        return Mono.empty();
    }

}
