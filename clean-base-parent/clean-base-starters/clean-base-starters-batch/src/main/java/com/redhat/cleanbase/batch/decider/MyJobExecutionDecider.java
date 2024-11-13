package com.redhat.cleanbase.batch.decider;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.lang.NonNull;

public class MyJobExecutionDecider implements JobExecutionDecider {
    @Override
    public @NonNull FlowExecutionStatus decide(@NonNull JobExecution jobExecution, StepExecution stepExecution) {
        return new FlowExecutionStatus("abc");
    }
}