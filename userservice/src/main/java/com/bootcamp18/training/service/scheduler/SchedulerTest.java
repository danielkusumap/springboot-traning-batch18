package com.bootcamp18.training.service.scheduler;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SchedulerTest {

    @Scheduled(cron = "${scheduler.test.cron}")
    // method ini dijadwalkan oleh Spring Scheduler
    public void testScheduler() {
        System.out.println("testScheduler");
    }
}
