package com.redhat.cleanbase.gateway.filter.cipher.rewritefunc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.Function;

@Component
@Slf4j
public class DecRqBodyRewriteFunc implements RewriteFunction<String, String> {

    @Override
    public Publisher<String> apply(ServerWebExchange exchange, String oldStringData) {
        return transferJsonData(oldStringData, this::decodeData);
    }

    private Map<String, Object> decodeData(@NonNull Map<String, Object> data) {
        data.put("decode", true);
        return data;
    }

    public static Mono<String> transferJsonData(String oldStringData, Function<Map<String, Object>, Map<String, Object>> dataTransferFunc) {
        try {
            val objectMapper = new ObjectMapper();
            val oldData = objectMapper.readValue(oldStringData, new TypeReference<Map<String, Object>>() {
            });
            val newData = dataTransferFunc.apply(oldData);
            val newStringData = objectMapper.writeValueAsString(newData);
            return Mono.just(newStringData);
        } catch (JsonProcessingException e) {
            log.error("[transferJsonData] transfer data failed", e);
            return Mono.just(oldStringData);
        }
    }


}
