package com.redhat.cleanbase.common.closer;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class ResourceCloser implements AutoCloseable {
    private final String resourceName;
    @NonNull
    private final AutoCloseable autoCloseable;
    private final boolean isFailThrow;

    @Override
    public void close() throws Exception {
        try {
            val finalResourceName =
                    Optional.ofNullable(resourceName)
                            .orElse("unknown resource");
            log.info("close resource : {} .....", finalResourceName);
            autoCloseable.close();
            log.info("close resource success");
        } catch (Exception e) {
            log.error("close resource fail", e);
            if (isFailThrow) {
                throw e;
            }
        }
    }
}