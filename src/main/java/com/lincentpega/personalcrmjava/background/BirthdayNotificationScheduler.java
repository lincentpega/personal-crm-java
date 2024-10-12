package com.lincentpega.personalcrmjava.background;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class BirthdayNotificationScheduler {

    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.MINUTES)
    public void processNotifications() {

    }
}
