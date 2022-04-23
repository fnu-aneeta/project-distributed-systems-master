package com.distributedsystems.master.services;

import com.distributedsystems.master.entities.ConnectionStatusEntity;
import com.distributedsystems.master.enums.ConnectionStatus;
import com.distributedsystems.master.repos.ConnectionStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ConnectionStatusService {
    private final ConnectionStatusRepository connectionStatusRepository;

    @Value("${app.configs.heartbeat.endpoint}")
    private String heartBeatEndpoint;

    private void markConnectionStatus(final String email,
                                      final String clientIp,
                                      final String clientPort,
                                      final ConnectionStatus connectionStatus) {
        log.info("User: {} is {}", email, connectionStatus);
        final Optional<ConnectionStatusEntity> optConnectionStatus = connectionStatusRepository.findFirstByUserEmail(email);
        final String heartBeatUrl = String.format("%s:%s%s", clientIp, clientPort, heartBeatEndpoint);
        if (optConnectionStatus.isPresent()) {
            final ConnectionStatusEntity connectionStatusEntity = optConnectionStatus.get();
            connectionStatusEntity.setConnectionStatus(connectionStatus);
            connectionStatusEntity.setHeartBeatUrl(heartBeatUrl);
            connectionStatusRepository.save(connectionStatusEntity);
        } else {
            connectionStatusRepository.save(ConnectionStatusEntity.builder()
                    .connectionStatus(connectionStatus)
                    .userEmail(email)
                    .heartBeatUrl(heartBeatUrl)
                    .build());
        }
    }

    public void markConnectionStatusOnline(final String email, final String clientIp, final String clientPort) {
        markConnectionStatus(email, clientIp, clientPort, ConnectionStatus.ONLINE);
    }

    public void markConnectionStatusOffline(final String email, final String clientIp, final String clientPort) {
        markConnectionStatus(email, clientIp, clientPort, ConnectionStatus.OFFLINE);
    }
}
