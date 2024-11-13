package com.redhat.cleanbase.batch.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;

// todo annotation 方式
public class MyAnnoJobExecutionListener {

    @BeforeJob
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("beforeJob anno");
    }

    @AfterJob
    public void afterJob(JobExecution jobExecution) {
        System.out.println("afterJob anno");
    }
}