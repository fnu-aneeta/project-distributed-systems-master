package com.distributedsystems.master.controllers;

import com.distributedsystems.master.services.TestJmsProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.*;

@RequiredArgsConstructor
@RestController
public class TestController {

    private final TestJmsProducer testJmsProducer;

    @GetMapping("/api/v1/test/{queueName}")
    void test(@PathVariable final String queueName) {
        testJmsProducer.sendMessage("aneeta@yahoo.com", "Hey how are you doing");
    }
}
