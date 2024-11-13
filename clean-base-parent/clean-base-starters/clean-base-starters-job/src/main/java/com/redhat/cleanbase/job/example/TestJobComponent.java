package com.redhat.cleanbase.job.example;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class TestJobComponent {
    private final Scheduler scheduler;

    @PostConstruct
    public void init() throws SchedulerException, InterruptedException {
        System.out.println(scheduler.checkExists(new JobKey("job1")));
        System.out.println(scheduler.checkExists(new TriggerKey("trigger1")));
//        System.out.println(scheduler.deleteJob(new JobKey("job1")));
        System.out.println(scheduler.checkExists(new JobKey("job1")));
        System.out.println(scheduler.getSchedulerName());
        System.out.println(scheduler.getSchedulerInstanceId());
        val triggerKeys = scheduler.getTriggerKeys(GroupMatcher.anyTriggerGroup());
        System.out.println(triggerKeys);
        for (TriggerKey triggerKey : triggerKeys) {
            val trigger = scheduler.getTrigger(triggerKey);
            System.out.println(trigger.getClass().getName());
            val triggerState = scheduler.getTriggerState(triggerKey);
            System.out.println(triggerState);
        }

        val job1 = scheduler.getJobDetail(new JobKey("job1"));
        System.out.println(job1);

        val currentlyExecutingJobs = scheduler.getCurrentlyExecutingJobs();
        System.out.println(currentlyExecutingJobs);

        val metaData = scheduler.getMetaData();
        System.out.println(metaData);


        System.out.println(scheduler.getTriggersOfJob(new JobKey("job1")));
    }

    public void howToUse() throws SchedulerException, InterruptedException {
        // 新增 job , 必須是 storeDurably 的
//            scheduler.addJob();
        // 刪除所有 quartz job detail 和 trigger
        // 小心使得萬年船,不然後悔終生
//            scheduler.clear();
        // 檢查是否存在 job, 提供 job name & group
        val b = scheduler.checkExists(new JobKey("job1"));
        // 檢查是否存在 trigger , 提供 job name & group
        val b1 = scheduler.checkExists(new TriggerKey("trigger1"));
        // 刪除某 job , 提供 job name & group
        val b2 = scheduler.deleteJob(new JobKey(""));
        // 一次刪除多個 JOB,提供多組 job name & group
        val b3 = scheduler.deleteJobs(List.of(new JobKey("")));

        // 全域資料存取,只在 instance ,不會存到 db
        val context = scheduler.getContext();

        // schedule 名稱 代表一個排程集群 namespace
        val schedulerName = scheduler.getSchedulerName();
        // 用 group 表達式查詢符合的 trigger name & trigger group
        val triggerKeys1 = scheduler.getTriggerKeys(GroupMatcher.anyTriggerGroup());
        // 查出 trigger keys 對應的 triggers
        val triggerStream = triggerKeys1.stream().map((triggerKey) -> {
            try {
                return scheduler.getTrigger(triggerKey);
            } catch (SchedulerException e) {
                throw new RuntimeException(e);
            }
        });
        //  查詢 job detail,提供 job name & group
        val a = scheduler.getJobDetail(new JobKey("a"));
        // 查詢正在值行的 job 資訊
        val currentlyExecutingJobs = scheduler.getCurrentlyExecutingJobs();
        // 查詢 job 全部的群組
        val jobGroupNames = scheduler.getJobGroupNames();
        // 用 group 表達式的 job keys , 跟 trigger 用法差不多
        scheduler.getJobKeys(GroupMatcher.jobGroupContains(""));

        // 新增 listener 去監聽事件, 可以搭配 websocket 去將事件發送給前端
        val listenerManager = scheduler.getListenerManager();
        listenerManager.addJobListener(null);
        listenerManager.addSchedulerListener(null);
        listenerManager.addTriggerListener(null);
        // scheduler 相關元數據
        val metaData = scheduler.getMetaData();
        // 查詢被暫停的 trigger groups
        val pausedTriggerGroups = scheduler.getPausedTriggerGroups();
        // 查詢 instance id
        val schedulerInstanceId = scheduler.getSchedulerInstanceId();
        // 查詢 特定 trigger,提供 trigger name & group
        val trigger1 = scheduler.getTrigger(new TriggerKey(""));
        // 查詢 特定 trigger 狀態,提供 trigger name & group
        val triggerState = scheduler.getTriggerState(new TriggerKey(""));

        //  查詢所有 trigger groups
        val triggerGroupNames = scheduler.getTriggerGroupNames();
        // 查詢這個 job 有多個 triggers,提供 job name & job group
        val g = scheduler.getTriggersOfJob(new JobKey("g"));
        // 打斷某個 job,但job一定要是 InterruptableJob 的實做類
        // 不是的化就會拋例外,提供 job name & group
        val interrupt = scheduler.interrupt(new JobKey(""));
        // 打斷某個 instanceId 正在執行的第一個 InterruptableJob job
        // 不是的化就會拋例外,提供 job name & group
        scheduler.interrupt("某個 instance");
        // 判斷 scheduler 是否被 shutdown
        val shutdown = scheduler.isShutdown();
        // 判斷 scheduler 是否已經開啟
        val started = scheduler.isStarted();
        // scheduler 活著但不調度 job
        val inStandbyMode = scheduler.isInStandbyMode();
        // 將組件狀態改成 pause , 不會觸發 job 啟動
        // 如果運行中的 job 不知道可不可以暫停
        // 使用 thread.stop suspend resume
        scheduler.pauseAll();
        scheduler.pauseJob(new JobKey(""));
        scheduler.pauseJobs(GroupMatcher.anyGroup());
        scheduler.pauseTrigger(new TriggerKey(""));
        scheduler.pauseTriggers(GroupMatcher.anyGroup());
        // 將 job 做恢復
        scheduler.resumeJob(new JobKey(""));
        scheduler.resumeJobs(GroupMatcher.anyGroup());
        scheduler.resumeAll();
        scheduler.resumeTrigger(new TriggerKey(""));
        scheduler.resumeTriggers(GroupMatcher.anyGroup());

        // todo 將 error state 轉成 state_waiting or state_pause
        scheduler.resetTriggerFromErrorState(new TriggerKey(""));
        // 註冊 trigger , 如有相同 trigger name & group 則取代原來 trigger
        scheduler.rescheduleJob(new TriggerKey(""), trigger1);
        // 註冊 job, 但不能重複 job name & group
//        scheduler.scheduleJob(jobDetail, trigger);
        // 直接將排程器關閉但會等 job 都做完
        scheduler.shutdown(true);
        // 變成待機模式
        scheduler.standby();
        // 直接觸發 job 執行, web api 觸發
        scheduler.triggerJob(new JobKey(""), new JobDataMap());
        // 取消註冊 trigger
        scheduler.unscheduleJob(new TriggerKey(""));
        scheduler.unscheduleJobs(List.of(new TriggerKey("")));

        Thread.sleep(10000000);

        scheduler.shutdown();
    }


}
