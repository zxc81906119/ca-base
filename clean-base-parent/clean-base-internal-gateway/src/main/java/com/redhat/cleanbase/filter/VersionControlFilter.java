package com.redhat.cleanbase.filter;

import com.redhat.cleanbase.constant.OrderConstants;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Observed
@RequiredArgsConstructor
@Slf4j
@Component
@Order(OrderConstants.VERSION_CONTROL_FILTER_ORDER)
public class VersionControlFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info(getClass().getSimpleName());
        return chain.filter(exchange);
    }

}
