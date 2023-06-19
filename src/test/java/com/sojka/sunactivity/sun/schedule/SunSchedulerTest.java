package com.sojka.sunactivity.sun.schedule;

import com.sojka.sunactivity.SunActivityApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@SpringJUnitConfig(SunSchedulerConfig.class)
@ActiveProfiles("sunScheduler")
@SpringBootTest(classes = SunActivityApplication.class)
class SunSchedulerTest {

    @SpyBean
    private SunScheduler scheduler;

    @Test
    void dailyCmeJob_cronForEveryTwoSecondAndFiveSecondsWaiting_atLeastCalledTwice() {
        await()
                .atMost(Duration.ofSeconds(5))
                .untilAsserted(() -> verify(scheduler, atLeast(2)).dailyCmeJob());
    }
}