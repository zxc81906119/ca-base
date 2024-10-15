package com.redhat.cleanbase.gateway.util;

import lombok.val;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

public final class SessionUtils {
    private SessionUtils() {
        throw new UnsupportedOperationException();
    }

    public static Optional<WebSession> getWebSessionSync(ServerWebExchange exchange) {
        return getWebSessionSync(exchange, null);
    }

    public static Optional<WebSession> getWebSessionSync(ServerWebExchange exchange, Executor executor) {
        try {
            return getWebSessionReactive(exchange)
                    .blockOptional();
        } catch (RuntimeException runtimeException) {
            try {
                val webSessionAsync = getWebSessionAsync(exchange, executor);
                return Optional.ofNullable(webSessionAsync.get());
            } catch (InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
                return Optional.empty();
            }
        }
    }

    public static CompletableFuture<WebSession> getWebSessionAsync(ServerWebExchange exchange, Executor executor) {
        val webSessionSupplier = (Supplier<WebSession>)
                () -> getWebSessionReactive(exchange)
                        .block();
        return executor != null ?
                CompletableFuture.supplyAsync(webSessionSupplier, executor)
                : CompletableFuture.supplyAsync(webSessionSupplier);
    }

    public static Mono<WebSession> getWebSessionReactive(ServerWebExchange exchange) {
        return exchange.getSession();
    }
}
