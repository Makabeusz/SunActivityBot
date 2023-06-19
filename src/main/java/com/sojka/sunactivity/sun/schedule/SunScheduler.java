package com.sojka.sunactivity.sun.schedule;

import com.sojka.sunactivity.sun.SunActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SunScheduler {

    private final SunActivityService service;

    @Scheduled(cron = "${sun.social.post.schedule.cron}")
    public void dailyCmeJob() {
        service.getAndPostCmeData();
    }
}
