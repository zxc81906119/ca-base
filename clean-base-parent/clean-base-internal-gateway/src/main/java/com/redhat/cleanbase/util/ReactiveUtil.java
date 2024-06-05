package com.redhat.cleanbase.util;

import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.concurrent.Callable;

public final class ReactiveUtil {
    private ReactiveUtil() {
        throw new UnsupportedOperationException();
    }

    public static <O> Mono<O> callFuncAndGetMono(Callable<O> callable) {
        try {
            return Optional.ofNullable(callable.call())
                    .map(Mono::just)
                    .orElseGet(Mono::empty);
        } catch (Exception e) {
            return Mono.error(e);
        }
    }
}
