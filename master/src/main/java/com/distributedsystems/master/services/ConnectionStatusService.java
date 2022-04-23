package com.distributedsystems.master.services;

import com.distributedsystems.master.entities.ClientConnectionDetailsEntity;
import com.distributedsystems.master.enums.ConnectionStatus;
import com.distributedsystems.master.repos.ClientConnectionDetailsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ConnectionStatusService {
    private final ClientConnectionDetailsRepository clientConnectionDetailsRepository;

    @Value("${app.configs.heartbeat.endpoint}")
    private String heartBeatEndpoint;

    @Value("${app.configs.heartbeat.protocol}")
    private String protocol;

    private void updateConnectionStatus(final String email,
                                        final String clientIp,
                                        final String clientPort,
                                        final ConnectionStatus connectionStatus) {
        log.info("User: {} is {}", email, connectionStatus);
        final Optional<ClientConnectionDetailsEntity> optConnectionStatus = clientConnectionDetailsRepository.findFirstByUserEmail(email);
        final String heartBeatUrl = String.format("%s://%s:%s%s", protocol, clientIp, clientPort, heartBeatEndpoint);
        if (optConnectionStatus.isPresent()) {
            final ClientConnectionDetailsEntity clientConnectionDetailsEntity = optConnectionStatus.get();
            clientConnectionDetailsEntity.setConnectionStatus(connectionStatus);
            clientConnectionDetailsEntity.setHeartBeatUrl(heartBeatUrl);
            clientConnectionDetailsRepository.save(clientConnectionDetailsEntity);
        } else {
            clientConnectionDetailsRepository.save(ClientConnectionDetailsEntity.builder()
                    .connectionStatus(connectionStatus)
                    .userEmail(email)
                    .heartBeatUrl(heartBeatUrl)
                    .build());
        }
    }

    public void markConnectionStatusOnline(final String email, final String clientIp, final String clientPort) {
        updateConnectionStatus(email, clientIp, clientPort, ConnectionStatus.ONLINE);
    }

    public void markConnectionStatusOffline(final String email, final String clientIp, final String clientPort) {
        updateConnectionStatus(email, clientIp, clientPort, ConnectionStatus.OFFLINE);
    }

    public void updateConnectionStatus(final ClientConnectionDetailsEntity connectionEntity,
                                       final ConnectionStatus connectionStatus) {
        connectionEntity.setConnectionStatus(connectionStatus);
        this.clientConnectionDetailsRepository.save(connectionEntity);
    }

    public List<ClientConnectionDetailsEntity> findAllClientsConnectionDetails(){
        return this.clientConnectionDetailsRepository.findAll();
    }
}
