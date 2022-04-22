package com.distributedsystems.master.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@EnableScheduling
@Configuration
public class AppConfigs {
    @Bean
    RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
