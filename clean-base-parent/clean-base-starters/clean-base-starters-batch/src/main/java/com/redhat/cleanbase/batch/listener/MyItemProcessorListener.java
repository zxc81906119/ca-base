package com.redhat.cleanbase.batch.listener;

import org.springframework.batch.core.ItemProcessListener;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public class MyItemProcessorListener implements ItemProcessListener<String, String> {
    @Override
    public void beforeProcess(@NonNull String item) {
        System.out.println("beforeProcess");
    }

    @Override
    public void afterProcess(@NonNull String item, @Nullable String result) {
        System.out.println("afterProcess");
    }

    @Override
    public void onProcessError(@NonNull String item, @NonNull Exception e) {
        System.out.println("onProcessError");
    }
}