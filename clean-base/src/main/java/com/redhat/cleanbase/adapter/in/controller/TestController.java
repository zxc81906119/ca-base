package com.redhat.cleanbase.adapter.in.controller;

import com.redhat.cleanbase.adapter.in.controller.model.TestDto;
import com.redhat.cleanbase.common.config.TestConfig;
import com.redhat.cleanbase.common.response.GenericResponse;
import com.redhat.cleanbase.common.trace.TracerWrapper;
import io.micrometer.observation.annotation.Observed;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Observed
@Slf4j
@RequestMapping("/test")
@RestController
@RequiredArgsConstructor
public class TestController {

    private final ExecutorService executorService = Executors.newFixedThreadPool(2 * Runtime.getRuntime().availableProcessors(), r -> {
        val thread = new Thread(r);
        thread.setName("pressureTest_" + thread.getId());
        return thread;
    });

    private final TracerWrapper tracerWrapper;
    private final TestConfig.TestProperties testProperties;

    @PreDestroy
    void close() {
        executorService.shutdown();
    }

    @PostMapping
    public GenericResponse<TestDto.Res> test(@RequestBody TestDto.Req req) {

        val region = testProperties.getRegion();

        val threads = Optional.ofNullable(req.getThreads())
                .orElse(1);
        val loop = Optional.ofNullable(req.getLoop())
                .orElse(1);
        val logEnabled = !Boolean.FALSE.equals(req.getLog());

        log.info("[threads:{}]", threads);
        log.info("[loop:{}]", loop);
        log.info("[logEnabled:{}]", logEnabled);

        val countDownLatch = new CountDownLatch(threads);
        run(threads, loop, logEnabled, countDownLatch, region);

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            log.error("interrupted", e);
        }

        val res = TestDto.Res.builder()
                .region(region)
                .build();

        return GenericResponse.ok(res, tracerWrapper.getCurrentSpanTraceId());

    }

    private void run(Integer threads, Integer loop, boolean logEnabled, CountDownLatch countDownLatch, String region) {
        for (int i = 0; i < threads; i++) {
            CompletableFuture.runAsync(
                    () -> {
                        try {
                            runLoop(loop, logEnabled, region);
                        } finally {
                            countDownLatch.countDown();
                        }
                    },
                    executorService);
        }
    }

    private static void runLoop(Integer loop, boolean logEnabled, String region) {
        for (int i1 = 0; i1 < loop; i1++) {
            if (logEnabled) {
                log.info("[region: {}] [loop:{}]", region, i1);
            }
        }
    }


}
