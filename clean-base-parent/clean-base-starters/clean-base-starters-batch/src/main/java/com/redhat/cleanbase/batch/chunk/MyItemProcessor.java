package com.redhat.cleanbase.batch.chunk;

import lombok.val;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;

public class MyItemProcessor implements ItemProcessor<String, String> {

    @Override
    public String process(@NonNull String item) throws Exception {
        val s = "prefix" + item;
        System.out.println("process:" + s);
        return s;
    }
}