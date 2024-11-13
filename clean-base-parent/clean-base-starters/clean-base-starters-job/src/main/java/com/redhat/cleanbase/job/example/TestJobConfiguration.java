package com.redhat.cleanbase.job.example;

import lombok.val;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

import static org.quartz.JobBuilder.newJob;

@Configuration
public class TestJobConfiguration {
    
    @Bean
    public JobDetail testJobDetail() {
        // define the job and tie it to our HelloJob class
        val jobDetailExecData = new JobDataMap();
        jobDetailExecData.put("k1", "v1");
        // 創建 job 類別是 HelloJob
        return newJob(TestHelloJob.class)
                // 沒有指定 group 預設 DEFAULT
                .withIdentity("job1")
                // 當 quartz 因為某些因素沒執行 job, 會進行重作
                .requestRecovery(true)
                // 放入 job 執行資料 map
                .setJobData(jobDetailExecData)
                // 放入 job data key value
                .usingJobData("k1", "v2")
                .usingJobData("k2", "v2")
                // 如果沒有 trigger 關聯此 job , 還是會保留 job 資訊
                // 手動觸發用,利於手動觸發 job , 搭配 web api
                .storeDurably(true)
                // job 描述
                .withDescription("測試 job")
                .build();
    }

    @Bean
    public Trigger testTrigger() {
        return TriggerBuilder.newTrigger()
                // 超過此時間就不會再觸發
                .endAt(new Date(System.currentTimeMillis() + 10000))
                // 對應至關聯的 job,如果 group 沒有指定,預設是 DEFAULT
//                    .forJob("a")
                .forJob(testJobDetail())
//                    .forJob(new JobKey("a", "b"))
                // 指定 trigger name & group
                // 沒有指定 group 預設 DEFAULT
                .withIdentity("trigger1")
                // 被 scheduler 排定後直接生效
                .startNow()
                // 某時間開始生效,生效之前都不會觸發對應的 job
//                    .startAt(new Date(System.currentTimeMillis() + 1000))
                // 可以覆蓋 job detail 的執行資料
                // todo 測試可否蓋過
                .usingJobData("k1", "v3")
                // 描述 trigger
                .withDescription("測試 trigger")
                // todo 觸發時機設定
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInSeconds(1)
                                .repeatForever()
                )
                .build();
    }


}


