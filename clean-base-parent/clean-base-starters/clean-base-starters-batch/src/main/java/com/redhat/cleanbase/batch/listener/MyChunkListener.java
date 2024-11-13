package com.redhat.cleanbase.batch.listener;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.lang.NonNull;

public class MyChunkListener implements ChunkListener {
    @Override
    public void beforeChunk(@NonNull ChunkContext context) {
        System.out.println("beforeChunk");
    }

    @Override
    public void afterChunk(@NonNull ChunkContext context) {
        System.out.println("afterChunk");
    }

    @Override
    public void afterChunkError(@NonNull ChunkContext context) {
        System.out.println("afterChunkError");
    }
}