package com.telus.codersplatform.userservice;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @CrossOrigin(origins = "*")
    @PostMapping("/register")
    public void registerUser(@RequestBody UserRegistrationRequest userRegistrationRequest) {
        log.info("new user registration {}", userRegistrationRequest);
        userService.registerUser(userRegistrationRequest);
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('user')")
    @CrossOrigin(origins = "*")
    public String getUserAuth() {
        return "This is for user";
    }



    @GetMapping("/admin")
    @PreAuthorize("hasRole('admin')")
    public String getAdminAuth() {
        return "This is for admin";
    }


}
