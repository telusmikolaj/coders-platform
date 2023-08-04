package com.telus.codersplatform.authservice.controller;

import com.telus.codersplatform.authservice.model.UserLoginRequest;
import com.telus.codersplatform.authservice.model.UserRegistrationRequest;
import com.telus.codersplatform.authservice.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @CrossOrigin(origins = "*")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationRequest registrationRequest) {
        return authService.createUser(registrationRequest);

    }
    @CrossOrigin(origins = "*")
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginRequest loginRequest) {
        try {
            return authService.loginUser(loginRequest);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}
