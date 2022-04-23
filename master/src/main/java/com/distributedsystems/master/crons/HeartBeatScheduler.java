package com.distributedsystems.master.crons;

import com.distributedsystems.master.services.HeartBeatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Slf4j
@RequiredArgsConstructor
@Component
public class HeartBeatScheduler {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private final HeartBeatService heartBeatService;

    @Scheduled(fixedRateString = "${app.configs.heartbeat.interval}")
    public void checkUsersHeartBeat() {
        log.info("Scheduled Job: Checking user's heat beat.");
        heartBeatService.checkAllUsersHeartBeat();
    }
}
