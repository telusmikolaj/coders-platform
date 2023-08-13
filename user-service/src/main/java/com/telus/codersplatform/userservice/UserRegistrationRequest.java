package com.telus.codersplatform.userservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationRequest {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String roles;
    private Long creationTimestamp;


}
