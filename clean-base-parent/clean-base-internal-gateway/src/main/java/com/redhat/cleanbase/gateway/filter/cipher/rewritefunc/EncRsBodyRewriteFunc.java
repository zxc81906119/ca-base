package com.redhat.cleanbase.gateway.filter.cipher.rewritefunc;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Map;

@Component
@Slf4j
public class EncRsBodyRewriteFunc implements RewriteFunction<String, String> {

    @Override
    public Publisher<String> apply(ServerWebExchange exchange, String oldStringData) {
        return DecRqBodyRewriteFunc.transferJsonData(oldStringData, this::encodeData);
    }

    private Map<String, Object> encodeData(@NonNull Map<String, Object> data) {
        data.put("encode", true);
        return data;
    }

}
