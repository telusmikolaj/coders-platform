package com.telus.codersplatform.userservice;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {

    private Long id;
    private String username;
    private String password;
    private String email;
    private Role role;

}
