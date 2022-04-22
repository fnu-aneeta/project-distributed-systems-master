package com.distributedsystems.master.controllers;

import com.distributedsystems.master.model.UserResource;
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
    UserResource createNewUser(@RequestBody @Valid UserResource userResource,
                               @RequestHeader("x-client-host") String host){

        final UserResource updateUserResource = userService.createNewUser(userResource);
        connectionStatusService.markConnectionStatusOnline(userResource.getEmail(), host);
        return updateUserResource;
    }
}
