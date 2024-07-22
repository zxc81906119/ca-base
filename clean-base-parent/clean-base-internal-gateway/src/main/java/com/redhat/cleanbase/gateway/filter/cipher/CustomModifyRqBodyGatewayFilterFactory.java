package com.redhat.cleanbase.gateway.filter.cipher;

import lombok.Data;
import lombok.val;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class CustomModifyRqBodyGatewayFilterFactory extends AbstractGatewayFilterFactory<CustomModifyRqBodyGatewayFilterFactory.Config> {
    private final ModifyRequestBodyGatewayFilterFactory modifyRequestBodyGatewayFilterFactory;
    private final ApplicationContext applicationContext;

    public CustomModifyRqBodyGatewayFilterFactory(
            ModifyRequestBodyGatewayFilterFactory modifyRequestBodyGatewayFilterFactory
            , ApplicationContext applicationContext
    ) {
        super(Config.class);
        this.modifyRequestBodyGatewayFilterFactory = modifyRequestBodyGatewayFilterFactory;
        this.applicationContext = applicationContext;
    }

    @Override
    public GatewayFilter apply(Config inputConfig) {
        return (serverWebExchange, filterChain) ->
                modifyRequestBodyGatewayFilterFactory
                        .apply(getConfigSetter(inputConfig))
                        .filter(serverWebExchange, filterChain);
    }

    public Consumer<ModifyRequestBodyGatewayFilterFactory.Config> getConfigSetter(Config inputConfig) {
        return (config) -> {
            config.setContentType(MediaType.APPLICATION_JSON_VALUE);
            val rewriteFunctionClass = inputConfig.getRewriteFunctionClass();
            val rewriteFunction = applicationContext.getBean(rewriteFunctionClass);
            config.setRewriteFunction(String.class, String.class, rewriteFunction);
        };
    }

    @Data
    public static class Config {
        private Class<? extends RewriteFunction<String, String>> rewriteFunctionClass;
    }
}
