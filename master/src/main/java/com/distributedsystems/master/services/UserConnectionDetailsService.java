package com.distributedsystems.master.services;

import com.distributedsystems.master.entities.UserConnectionDetailsEntity;
import com.distributedsystems.master.enums.ConnectionStatus;
import com.distributedsystems.master.repos.UserConnectionDetailsRepository;
import com.distributedsystems.master.resources.UserConnectionDetailsResource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserConnectionDetailsService {
    private final UserConnectionDetailsRepository userConnectionDetailsRepository;

    @Value("${app.configs.heartbeat.endpoint}")
    private String heartBeatEndpoint;

    @Value("${app.configs.heartbeat.protocol}")
    private String protocol;

    private void updateConnectionStatus(final String email,
                                        final String userIp,
                                        final String userPort,
                                        final ConnectionStatus connectionStatus) {
        log.info("User: {} is {}", email, connectionStatus);
        final Optional<UserConnectionDetailsEntity> optConnectionStatus = userConnectionDetailsRepository.findFirstByUserEmail(email);
        final String heartBeatUrl = String.format("%s://%s:%s%s", protocol, userIp, userPort, heartBeatEndpoint);
        if (optConnectionStatus.isPresent()) {
            final UserConnectionDetailsEntity userConnectionDetailsEntity = optConnectionStatus.get();
            userConnectionDetailsEntity.setConnectionStatus(connectionStatus);
            userConnectionDetailsEntity.setHeartBeatUrl(heartBeatUrl);
            userConnectionDetailsRepository.save(userConnectionDetailsEntity);
        } else {
            userConnectionDetailsRepository.save(UserConnectionDetailsEntity.builder()
                    .connectionStatus(connectionStatus)
                    .userEmail(email)
                    .heartBeatUrl(heartBeatUrl)
                    .build());
        }
    }

    public void markConnectionStatusOnline(final String email, final String userIp, final String userPort) {
        updateConnectionStatus(email, userIp, userPort, ConnectionStatus.ONLINE);
    }

    public void markConnectionStatusOffline(final String email, final String userIp, final String userPort) {
        updateConnectionStatus(email, userIp, userPort, ConnectionStatus.OFFLINE);
    }

    public void updateConnectionStatus(final UserConnectionDetailsResource userConnectionDetails,
                                       final ConnectionStatus connectionStatus) {
        final Optional<UserConnectionDetailsEntity> optUserConnectionDetailsEntity = this.userConnectionDetailsRepository.findFirstByUserEmail(userConnectionDetails.getUserEmail());
        if(optUserConnectionDetailsEntity.isEmpty()){
            log.error("Cannot update connection status for user: {}, unable to find user", userConnectionDetails.getUserEmail());
            return;
        }

        final UserConnectionDetailsEntity userConnectionDetailsEntity = optUserConnectionDetailsEntity.get();
        userConnectionDetailsEntity.setConnectionStatus(connectionStatus);
        this.userConnectionDetailsRepository.save(userConnectionDetailsEntity);
    }

    private void userConnectionDetailsEntity2Resource(final UserConnectionDetailsResource userConnectionDetailsResource,
                                                      final UserConnectionDetailsEntity userConnectionDetailsEntity){
        if(ObjectUtils.isEmpty(userConnectionDetailsResource) || ObjectUtils.isEmpty(userConnectionDetailsEntity)){
            return;
        }

        userConnectionDetailsResource.setUserEmail(userConnectionDetailsEntity.getUserEmail());
        userConnectionDetailsResource.setHeartBeatUrl(userConnectionDetailsEntity.getHeartBeatUrl());
        userConnectionDetailsResource.setConnectionStatus(userConnectionDetailsEntity.getConnectionStatus());
    }

    public List<UserConnectionDetailsResource> findAllUsersConnectionDetails(){
        final List<UserConnectionDetailsEntity> userConnectionDetailsEntities = this.userConnectionDetailsRepository.findAll();

        final List<UserConnectionDetailsResource> userConnectionDetailsResources = new LinkedList<>();
        if(!ObjectUtils.isEmpty(userConnectionDetailsEntities)){
            userConnectionDetailsEntities.forEach(userConnectionDetailsEntity -> {
                final UserConnectionDetailsResource userConnectionDetailsResource = new UserConnectionDetailsResource();
                userConnectionDetailsEntity2Resource(userConnectionDetailsResource, userConnectionDetailsEntity);
                userConnectionDetailsResources.add(userConnectionDetailsResource);
            });
        }

        return userConnectionDetailsResources;
    }
}
