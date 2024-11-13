package com.redhat.cleanbase.batch.chunk;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

public class MyItemWriter implements ItemWriter<String> {
    @Override
    public void write(Chunk<? extends String> chunk) throws Exception {
        System.out.println("writer:" + chunk.getItems());
    }
}