package com.redhat.cleanbase.batch.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.lang.NonNull;

public class MyJobExecutionListener implements JobExecutionListener {

    @Override
    public void beforeJob(@NonNull JobExecution jobExecution) {
        System.out.println("beforeJob");
    }

    @Override
    public void afterJob(@NonNull JobExecution jobExecution) {
        System.out.println("afterJob");
    }
}