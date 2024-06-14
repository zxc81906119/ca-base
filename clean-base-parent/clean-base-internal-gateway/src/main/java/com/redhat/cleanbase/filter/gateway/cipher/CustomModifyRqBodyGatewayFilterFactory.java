package com.redhat.cleanbase.filter.gateway.cipher;

import com.redhat.cleanbase.filter.gateway.cipher.rewritefunc.DecRqBodyRewriteFunc;
import lombok.Data;
import lombok.val;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class CustomModifyRqBodyGatewayFilterFactory extends AbstractGatewayFilterFactory<CustomModifyRqBodyGatewayFilterFactory.Config> {
    private final ModifyRequestBodyGatewayFilterFactory modifyRequestBodyGatewayFilterFactory;
    private final DecRqBodyRewriteFunc decRqBodyRewriteFunc;

    public CustomModifyRqBodyGatewayFilterFactory(
            ModifyRequestBodyGatewayFilterFactory modifyRequestBodyGatewayFilterFactory
            , DecRqBodyRewriteFunc decRqBodyRewriteFunc
    ) {
        super(Config.class);
        this.modifyRequestBodyGatewayFilterFactory = modifyRequestBodyGatewayFilterFactory;
        this.decRqBodyRewriteFunc = decRqBodyRewriteFunc;
    }

    @Override
    public GatewayFilter apply(Config inputConfig) {
        return (serverWebExchange, filterChain) -> {
            val headers = serverWebExchange.getRequest().getHeaders();
            val contentType = headers.getFirst(HttpHeaders.CONTENT_TYPE);
            if (contentType != null && contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
                return modifyRequestBodyGatewayFilterFactory
                        .apply((config) -> {
                            config.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            config.setRewriteFunction(String.class, String.class, decRqBodyRewriteFunc);
                        })
                        .filter(serverWebExchange, filterChain);
            }
            return filterChain.filter(serverWebExchange);
        };
    }

    @Data
    public static class Config {
    }

}
