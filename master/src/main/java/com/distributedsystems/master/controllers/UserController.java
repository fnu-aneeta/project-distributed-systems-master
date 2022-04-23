package com.distributedsystems.master.controllers;

import com.distributedsystems.master.resources.UserResource;
import com.distributedsystems.master.services.ConnectionStatusService;
import com.distributedsystems.master.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;
    private final ConnectionStatusService connectionStatusService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserResource createUser(@RequestBody @Valid UserResource userResource,
                               @RequestHeader("x-client-ip") String clientIp,
                            @RequestHeader("x-client-port") String clientPort) {

        final UserResource updateUserResource = userService.createNewUser(userResource);
        connectionStatusService.markConnectionStatusOnline(userResource.getEmail(), clientIp, clientPort);
        return updateUserResource;
    }

    @PutMapping
    UserResource updateUser(@RequestBody @Valid UserResource userResource,
                               @RequestHeader("x-client-ip") String clientIp,
                            @RequestHeader("x-client-port") String clientPort){

        final UserResource updateUserResource = userService.updateUser(userResource);
        connectionStatusService.markConnectionStatusOnline(userResource.getEmail(), clientIp, clientPort);
        return updateUserResource;
    }
}
