package com.redhat.cleanbase.batch.config;

import com.redhat.cleanbase.batch.chunk.MyItemProcessor;
import com.redhat.cleanbase.batch.chunk.MyItemReader;
import com.redhat.cleanbase.batch.chunk.MyItemWriter;
import com.redhat.cleanbase.batch.decider.MyJobExecutionDecider;
import com.redhat.cleanbase.batch.incrementer.DailyIdIncrementer;
import com.redhat.cleanbase.batch.listener.*;
import com.redhat.cleanbase.batch.param.validator.ParamValidator;
import com.redhat.cleanbase.batch.tasklet.MyTasklet;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.CompositeJobExecutionListener;
import org.springframework.batch.core.listener.JobListenerFactoryBean;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@RequiredArgsConstructor
@Configuration
@Import(SecondJobConfig.class)
@ComponentScan("com.redhat.cleanbase.batch")
public class BatchAutoConfig {
    private final TaskExecutor taskExecutor;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;


    @Bean
    public Tasklet simpleStepImpl() {
        return new MyTasklet(null);
    }

    @Bean
    public MyChunkListener myChunkListener() {
        return new MyChunkListener();
    }

    @Bean
    public MyItemReader myItemReader() {
        return new MyItemReader();
    }

    @Bean
    public MyItemReaderListener myItemReaderListener() {
        return new MyItemReaderListener();
    }

    @Bean
    public MyItemProcessor myItemProcessor() {
        return new MyItemProcessor();
    }

    @Bean
    public MyItemWriter myItemWriter() {
        return new MyItemWriter();
    }

    @Bean
    public MyItemWriterListener myItemWriterListener() {
        return new MyItemWriterListener();
    }


    @Bean
    public MyStepListener myStepListener() {
        return new MyStepListener();
    }

    @Bean
    public Step exampleStep() {
        return new StepBuilder("exampleStep", jobRepository)
                .tasklet(simpleStepImpl(), transactionManager)
                .build();
    }

    @Bean
    public Step exampleStep01() {
        return new StepBuilder("exampleStep01", jobRepository)
                .<String, String>chunk(3, transactionManager)
                .taskExecutor(taskExecutor)
                .reader(myItemReader())
                .writer(myItemWriter())
                .processor(myItemProcessor())
                .listener(myStepListener())
                .listener(myChunkListener())
                .listener(myItemReaderListener())
                .listener(myItemProcessorListener())
                .listener(myItemWriterListener())
                .build();
    }


    @Bean
    public Step flowStep1() {
        return new StepBuilder("flowStep1", jobRepository)
                .tasklet((contribution, context) -> {
                    System.out.println("flowStep1");
                    contribution.setExitStatus(ExitStatus.COMPLETED);
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step flowStep2() {
        return new StepBuilder("flowStep2", jobRepository)
                .tasklet((contribution, context) -> {
                    System.out.println("flowStep2");
                    context.getStepContext().getStepExecution().setTerminateOnly();
                    contribution.setExitStatus(ExitStatus.COMPLETED);
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }


    @Bean
    public Flow exampleFlow() {
        return new FlowBuilder<Flow>("example-flow")
                .start(flowStep1())
                .next(flowStep2())
                .build();
    }

    @Bean
    public Job exampleJob() {
        return new JobBuilder("exampleJob", jobRepository)
                .start(exampleStep01())
                .on(ExitStatus.FAILED.getExitCode()).to(myJobExecutionDecider())
                .from(myJobExecutionDecider()).on("abc").to(exampleFlow())
                .build()
                .incrementer(dailyIdIncrementer())
                .validator(compositeJobParametersValidator())
                .listener(compositeJobExecutionListener())
                .build();
    }

    @Bean
    public RunIdIncrementer runIdIncrementer() {
        return new RunIdIncrementer();
    }

    @Bean
    public DailyIdIncrementer dailyIdIncrementer() {
        return new DailyIdIncrementer();
    }

    @Bean
    public ParamValidator exampleJobParamValidator() {
        return new ParamValidator();
    }

    @Bean
    public DefaultJobParametersValidator defaultJobParametersValidator() {
        return new DefaultJobParametersValidator(new String[]{"name"}, new String[]{"age"});
    }

    @Bean
    public CompositeJobParametersValidator compositeJobParametersValidator() {
        val compositeJobParametersValidator = new CompositeJobParametersValidator();
        compositeJobParametersValidator.setValidators(
                List.of(
                        exampleJobParamValidator(),
                        defaultJobParametersValidator()
                )
        );
        return compositeJobParametersValidator;
    }

    @Bean
    public CompositeJobExecutionListener compositeJobExecutionListener() {
        val compositeJobExecutionListener = new CompositeJobExecutionListener();
        compositeJobExecutionListener.setListeners(
                List.of(
                        myJobExecutionListener(),
                        JobListenerFactoryBean.getListener(myAnnoJobExecutionListener())
                )
        );
        return compositeJobExecutionListener;
    }

    @Bean
    public MyJobExecutionListener myJobExecutionListener() {
        return new MyJobExecutionListener();
    }

    @Bean
    public MyAnnoJobExecutionListener myAnnoJobExecutionListener() {
        return new MyAnnoJobExecutionListener();
    }

    @Bean
    public MyItemProcessorListener myItemProcessorListener() {
        return new MyItemProcessorListener();
    }


    @Bean
    public MyJobExecutionDecider myJobExecutionDecider() {
        return new MyJobExecutionDecider();
    }

}
