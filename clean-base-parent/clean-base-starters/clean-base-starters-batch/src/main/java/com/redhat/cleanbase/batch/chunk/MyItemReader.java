package com.redhat.cleanbase.batch.chunk;

import lombok.val;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class MyItemReader implements ItemReader<String> {

    private volatile int now = 0;

    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        val totalCount = 20;
        if (now < totalCount) {
            synchronized (this) {
                if (now < totalCount) {
                    if (now++ == 14) {
                        throw new RuntimeException("gg");
                    }
                    val s = "gg" + now;
                    System.out.println("read:" + s);
                    return s;
                }
            }
        }
        System.out.println("超過上限");
        return null;
    }
}