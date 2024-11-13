package com.redhat.cleanbase.batch.tasklet;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

@RequiredArgsConstructor
public class MyTasklet implements Tasklet {

    private final String lazyData;
    private boolean hasDo = false;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        val stepContext = chunkContext.getStepContext();
        // job 中共享數據
        val jobExecutionContextData = stepContext.getJobExecutionContext();
        // step 中共享數據
        val stepExecutionContextData = stepContext.getStepExecutionContext();

        val stepExecution = stepContext.getStepExecution();
        val stepExecutionContext = stepExecution.getExecutionContext();

        val jobExecution = stepExecution.getJobExecution();
        val jobExecutionContext = jobExecution.getExecutionContext();

        if (!hasDo) {
            hasDo = true;
            throw new RuntimeException("執行 job 失敗");
        }
        // 執行參數
        val jobParameters = stepContext.getJobParameters();
        System.out.println("data:" + jobParameters);
        System.out.println("lazyData:" + lazyData);
        System.out.println("跑簡單的程式!!!");

        contribution.setExitStatus(ExitStatus.COMPLETED);
        return RepeatStatus.FINISHED;
    }
}