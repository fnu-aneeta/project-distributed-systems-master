package com.distributedsystems.master.controllers;

import com.distributedsystems.master.resources.UserConnectionDetailsResource;
import com.distributedsystems.master.services.UserConnectionDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserConnectionDetailsController {
    private final UserConnectionDetailsService userConnectionDetailsService;

    @GetMapping("/users/connection/status")
    List<UserConnectionDetailsResource> findAllUsersConnectionDetails(){
        return userConnectionDetailsService.findAllUsersConnectionDetails();
    }
}
