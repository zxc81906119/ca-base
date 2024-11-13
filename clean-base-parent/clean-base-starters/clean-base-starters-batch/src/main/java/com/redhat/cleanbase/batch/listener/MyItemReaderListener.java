package com.redhat.cleanbase.batch.listener;

import org.springframework.batch.core.ItemReadListener;
import org.springframework.lang.NonNull;

public class MyItemReaderListener implements ItemReadListener<String> {
    @Override
    public void beforeRead() {
        System.out.println("beforeRead");
    }

    @Override
    public void afterRead(@NonNull String item) {
        System.out.println("afterRead:" + item);
    }

    @Override
    public void onReadError(@NonNull Exception ex) {
        System.out.println("onReadError");
    }
}