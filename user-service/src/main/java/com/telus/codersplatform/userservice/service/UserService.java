package com.telus.codersplatform.userservice.service;

import com.telus.codersplatform.userservice.UserRepository;
import com.telus.codersplatform.userservice.mapper.UserRegistrationRequestMapper;
import com.telus.codersplatform.userservice.model.UserRegistrationRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final UserRegistrationRequestMapper userMapper;
    public void registerUser(UserRegistrationRequest userRegistrationRequest) {
        this.userRepository.save(this.userMapper.registrationRequestToUser(userRegistrationRequest));
        log.info("User sucessfully registered " + userRegistrationRequest);
    }
}
