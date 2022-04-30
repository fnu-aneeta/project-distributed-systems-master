package com.distributedsystems.master.services;

import com.distributedsystems.master.enums.ConnectionStatus;
import com.distributedsystems.master.resources.UserConnectionDetailsResource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class HeartBeatService {
    private final UserConnectionDetailsService userConnectionDetailsService;
    private final RestTemplate restTemplate;

    public void checkAllUsersHeartBeat() {
        final List<UserConnectionDetailsResource> usersConnectionDetails = userConnectionDetailsService.findAllUsersConnectionDetails();
        if (ObjectUtils.isEmpty(usersConnectionDetails)) {
            return;
        }

        log.info("Total number of users: {}", usersConnectionDetails.size());
        usersConnectionDetails.forEach(userConnectionDetails -> {
            final ConnectionStatus connectionStatus = findConnectionStatus(userConnectionDetails.getHeartBeatUrl());
            userConnectionDetailsService.updateConnectionStatus(userConnectionDetails, connectionStatus);
        });
    }

    public ConnectionStatus findConnectionStatus(final String heartBeatUrl) {
        try {
            ResponseEntity<String> response = restTemplate.exchange(heartBeatUrl, HttpMethod.GET, null, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                final String rawResponse = response.getBody();
                if (!ObjectUtils.isEmpty(rawResponse)) {
                    if (rawResponse.toUpperCase().contains("UP")) {
                        return ConnectionStatus.ONLINE;
                    }
                }
            }
        } catch (ResourceAccessException e) {
            log.error("Unable to build connection with master: {}", e.getMessage());
           // e.printStackTrace();
        } catch (Exception e) {
            log.error("Unable to build connection with master: {}", e.getMessage());
            //e.printStackTrace();
        }
        return ConnectionStatus.OFFLINE;
    }
}
