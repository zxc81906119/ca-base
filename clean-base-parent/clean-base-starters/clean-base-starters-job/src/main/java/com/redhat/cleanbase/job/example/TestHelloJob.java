package com.redhat.cleanbase.job.example;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

//@DisallowConcurrentExecution
//@PersistJobDataAfterExecution
public class TestHelloJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        System.out.println("hello");
    }
}