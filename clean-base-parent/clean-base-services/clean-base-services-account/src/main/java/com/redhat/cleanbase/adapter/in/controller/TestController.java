package com.redhat.cleanbase.adapter.in.controller;

import com.redhat.cleanbase.common.config.TestConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final TestConfig.TestProperties testProperties;

    @GetMapping("/region")
    public String getRegion() {
        return testProperties.getRegion();
    }

    @GetMapping({"/multi-thread/{count}", "/multi-thread"})
    public String processMultiThread(@PathVariable(required = false) Integer count) throws InterruptedException {
        val finalCount =
                Optional.ofNullable(count)
                        .or(() ->
                                Optional.of(testProperties)
                                        .map(TestConfig.TestProperties::getThread)
                                        .map(TestConfig.TestProperties.ThreadProperties::getCount)
                        )
                        .orElse(0);

        if (finalCount == 0) {
            return "no_open_thread";
        }
        val i1 = Runtime.getRuntime().availableProcessors();
        val executorService = Executors.newFixedThreadPool(i1 * 2);
        try {
            val countDownLatch = new CountDownLatch(finalCount);
            for (int i = 0; i < finalCount; i++) {
                final int finalI = i + 1;
                CompletableFuture.runAsync(
                        () -> {
                            try {

                                val name = Thread.currentThread().getName();
                                try {
                                    log.info("thread: {} , doAction: {} -> open", name, finalI);
                                    Thread.sleep((long) (Math.random() * 10000));
                                    log.info("thread: {} , doAction: {} -> end", name, finalI);
                                } catch (InterruptedException e) {
                                    log.info("thread: {} , doAction: {} -> interrupted", name, finalI);
                                }
                            } finally {
                                countDownLatch.countDown();
                            }
                        }
                        , executorService);
            }
            countDownLatch.await();
        } finally {
            executorService.shutdown();
        }
        return "success";
    }


}
