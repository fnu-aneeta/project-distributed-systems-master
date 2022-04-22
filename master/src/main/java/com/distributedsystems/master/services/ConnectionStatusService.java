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

    @Value("${app.config.heartbeat.endpoint}")
    private String heartBeatEndpoint;

    private void markConnectionStatus(final String email, final String host, final ConnectionStatus connectionStatus) {
        log.info("Updating connection status: {} for user: {}", connectionStatus, email);
        final Optional<ConnectionStatusEntity> optConnectionStatus = connectionStatusRepository.findFirstByUserEmail(email);
        if (optConnectionStatus.isPresent()) {
            final ConnectionStatusEntity connectionStatusEntity = optConnectionStatus.get();
            connectionStatusEntity.setConnectionStatus(connectionStatus);
            connectionStatusRepository.save(connectionStatusEntity);
        } else {
            connectionStatusRepository.save(ConnectionStatusEntity.builder()
                    .connectionStatus(connectionStatus)
                    .userEmail(email)
                    .heartBeatUrl(String.format("%s%s", host, heartBeatEndpoint))
                    .build());
        }
    }

    public void markConnectionStatusOnline(final String email, final String host) {
        markConnectionStatus(email, host, ConnectionStatus.ONLINE);
    }

    public void markConnectionStatusOffline(final String email, final String host) {
        markConnectionStatus(email, host, ConnectionStatus.OFFLINE);
    }
}
