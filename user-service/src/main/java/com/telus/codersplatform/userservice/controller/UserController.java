package com.telus.codersplatform.userservice.controller;


import com.telus.codersplatform.userservice.model.UserRegistrationRequest;
import com.telus.codersplatform.userservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public void createUser(@RequestBody UserRegistrationRequest userRegistrationRequest) {
        this.userService.registerUser(userRegistrationRequest);
    }
}
