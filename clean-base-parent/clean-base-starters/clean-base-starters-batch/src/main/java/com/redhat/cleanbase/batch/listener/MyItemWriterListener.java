package com.redhat.cleanbase.batch.listener;

import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;
import org.springframework.lang.NonNull;

public class MyItemWriterListener implements ItemWriteListener<String> {

    @Override
    public void beforeWrite(@NonNull Chunk<? extends String> items) {
        System.out.println("beforeWrite");
    }

    @Override
    public void afterWrite(@NonNull Chunk<? extends String> items) {
        System.out.println("afterWrite");
    }

    @Override
    public void onWriteError(@NonNull Exception exception, @NonNull Chunk<? extends String> items) {
        System.out.println("onWriteError");
    }

}