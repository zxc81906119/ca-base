package com.redhat.cleanbase.gateway.util;

import lombok.val;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;

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
        val webSessionAsync = getWebSessionAsync(exchange, executor);
        try {
            return Optional.ofNullable(webSessionAsync.get());
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            return Optional.empty();
        }
    }

    public static CompletableFuture<WebSession> getWebSessionAsync(ServerWebExchange exchange, Executor executor) {
        val webSessionSupplier = (Supplier<WebSession>) () -> exchange.getSession().block();
        return executor != null ?
                CompletableFuture.supplyAsync(webSessionSupplier, executor)
                : CompletableFuture.supplyAsync(webSessionSupplier);
    }
}
