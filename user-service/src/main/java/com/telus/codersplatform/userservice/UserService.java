package com.telus.codersplatform.userservice;

import org.springframework.stereotype.Service;

@Service
public class UserService {


    public void registerUser(UserRegistrationRequest userRegistrationRequest) {
        User user = User.builder()
                .username(userRegistrationRequest.username())
                .password(userRegistrationRequest.password())
                .email(userRegistrationRequest.email())
                .build();
    }
}
