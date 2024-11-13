package com.redhat.cleanbase.batch.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public class MyStepListener implements StepExecutionListener {
    @Override
    public void beforeStep(@NonNull StepExecution stepExecution) {
        System.out.println("beforeStep");
    }

    @Override
    @Nullable
    public ExitStatus afterStep(StepExecution stepExecution) {
        System.out.println("afterStep");
        return stepExecution.getExitStatus();
    }

}