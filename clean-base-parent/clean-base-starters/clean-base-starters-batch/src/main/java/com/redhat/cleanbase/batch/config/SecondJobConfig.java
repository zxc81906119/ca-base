package com.redhat.cleanbase.batch.config;

import com.redhat.cleanbase.batch.tasklet.MyTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@RequiredArgsConstructor
@Configuration
public class SecondJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Tasklet simpleStepImpl1() {
        return new MyTasklet(null);
    }

    @StepScope
    @Bean
    public Tasklet simpleStepImpl11(@Value("#{jobParameters['name']}") String name) {
        return new MyTasklet(name);
    }

    @Bean
    public Step exampleStep1() {
        return new StepBuilder("exampleStep1", jobRepository)
                .tasklet(simpleStepImpl11(null), transactionManager)
                .build();
    }

    @Bean
    public Job exampleJob1() {
        return new JobBuilder("exampleJob1", jobRepository)
                .start(exampleStep1())
                .build();
    }

}
