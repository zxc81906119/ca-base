package com.redhat.cleanbase.gateway.filter.cipher;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyResponseBodyGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

@Component
public class CustomModifyRsBodyGatewayFilterFactory extends AbstractGatewayFilterFactory<CustomModifyRsBodyGatewayFilterFactory.Config> {
    private final ModifyResponseBodyGatewayFilterFactory modifyResponseBodyGatewayFilterFactory;
    private final ApplicationContext applicationContext;

    public CustomModifyRsBodyGatewayFilterFactory(
            ModifyResponseBodyGatewayFilterFactory modifyResponseBodyGatewayFilterFactory
            , ApplicationContext applicationContext
    ) {
        super(Config.class);
        this.modifyResponseBodyGatewayFilterFactory = modifyResponseBodyGatewayFilterFactory;
        this.applicationContext = applicationContext;
    }

    @Override
    public GatewayFilter apply(Config inputConfig) {
        return new CustomModifyRsBodyGatewayFilter(
                modifyResponseBodyGatewayFilterFactory
                , inputConfig
                , applicationContext
        );
    }


    @Data
    public static class Config {
        private Class<? extends RewriteFunction<String, String>> rewriteFunctionClass;
    }

    @RequiredArgsConstructor
    public static class CustomModifyRsBodyGatewayFilter implements GatewayFilter, Ordered {
        private final ModifyResponseBodyGatewayFilterFactory modifyResponseBodyGatewayFilterFactory;
        private final CustomModifyRsBodyGatewayFilterFactory.Config config;
        private final ApplicationContext applicationContext;

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
            return modifyResponseBodyGatewayFilterFactory
                    .apply(getConfigSetter(config))
                    .filter(exchange, chain);
        }

        public Consumer<ModifyResponseBodyGatewayFilterFactory.Config> getConfigSetter(Config inputConfig) {
            return (config) -> {
                config.setNewContentType(MediaType.APPLICATION_JSON_VALUE);
                val rewriteFunctionClass = inputConfig.getRewriteFunctionClass();
                val rewriteFunction = applicationContext.getBean(rewriteFunctionClass);
                config.setRewriteFunction(String.class, String.class, rewriteFunction);
            };
        }

        @Override
        public int getOrder() {
            return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 1;
        }
    }
}
