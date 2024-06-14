package com.redhat.cleanbase.filter.gateway.cipher;

import com.redhat.cleanbase.filter.gateway.cipher.rewritefunc.EncRsBodyRewriteFunc;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyResponseBodyGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class CustomModifyRsBodyGatewayFilterFactory extends AbstractGatewayFilterFactory<CustomModifyRsBodyGatewayFilterFactory.Config> {
    private final ModifyResponseBodyGatewayFilterFactory modifyResponseBodyGatewayFilterFactory;
    private final EncRsBodyRewriteFunc encRsBodyRewriteFunc;

    public CustomModifyRsBodyGatewayFilterFactory(
            ModifyResponseBodyGatewayFilterFactory modifyResponseBodyGatewayFilterFactory
            , EncRsBodyRewriteFunc encRsBodyRewriteFunc
    ) {
        super(Config.class);
        this.modifyResponseBodyGatewayFilterFactory = modifyResponseBodyGatewayFilterFactory;
        this.encRsBodyRewriteFunc = encRsBodyRewriteFunc;
    }

    @Override
    public GatewayFilter apply(Config inputConfig) {
        return new CustomModifyRsBodyGatewayFilter(
                modifyResponseBodyGatewayFilterFactory
                , inputConfig
                , encRsBodyRewriteFunc
        );
    }

    @Data
    public static class Config {
    }

    @RequiredArgsConstructor
    public static class CustomModifyRsBodyGatewayFilter implements GatewayFilter, Ordered {
        private final ModifyResponseBodyGatewayFilterFactory modifyResponseBodyGatewayFilterFactory;
        private final CustomModifyRsBodyGatewayFilterFactory.Config config;
        private final EncRsBodyRewriteFunc encRsBodyRewriteFunc;

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
            return modifyResponseBodyGatewayFilterFactory
                    .apply((config) -> {
                        config.setNewContentType(MediaType.APPLICATION_JSON_VALUE);
                        config.setRewriteFunction(String.class, String.class, encRsBodyRewriteFunc);
                    })
                    .filter(exchange, chain);
        }

        @Override
        public int getOrder() {
            return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 1;
        }
    }
}
