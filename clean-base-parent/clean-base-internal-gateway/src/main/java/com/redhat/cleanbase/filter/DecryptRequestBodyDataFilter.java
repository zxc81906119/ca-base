package com.redhat.cleanbase.filter;

import com.redhat.cleanbase.constant.OrderConstant;
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
@Order(OrderConstant.DECRYPT_REQUEST_BODY_DATA_FILTER_ORDER_ORDER)
public class DecryptRequestBodyDataFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info(getClass().getSimpleName());
        return chain.filter(exchange);
    }
}
