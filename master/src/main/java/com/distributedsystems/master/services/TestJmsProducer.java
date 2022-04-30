package com.distributedsystems.master.services;

import com.distributedsystems.master.resources.TestMessageResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class TestJmsProducer {
    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    public void sendMessage(final String email, final String message){
        try{
            log.info("Attempting Send message to Queue: "+ email);
            final TestMessageResource testMessageResource = TestMessageResource.builder()
                    .fromEmail(email)
                    .message(message)
                    .build();
            final String jsonStringMessage = objectMapper.writeValueAsString(testMessageResource);
            jmsTemplate.convertAndSend(email, jsonStringMessage);
        } catch(Exception e){
            log.error("Exception during send Message: ", e);
        }
    }
}
