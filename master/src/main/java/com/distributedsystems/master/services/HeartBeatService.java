package com.distributedsystems.master.services;

import com.distributedsystems.master.repos.ConnectionStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class HeartBeatService {
    private final ConnectionStatusRepository connectionStatusRepository;


}
