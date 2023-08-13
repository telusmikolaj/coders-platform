package com.telus.codersplatform.userservice;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void registerUser(UserRegistrationRequest userRegistrationRequest) {

    }
}
