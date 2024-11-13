package com.redhat.cleanbase.batch.incrementer;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.lang.NonNull;

import java.util.Date;

public class DailyIdIncrementer implements JobParametersIncrementer {
    @Override
    public @NonNull JobParameters getNext(JobParameters parameters) {
        return new JobParametersBuilder(parameters)
                .addDate("date", new Date())
                .toJobParameters();
    }
}