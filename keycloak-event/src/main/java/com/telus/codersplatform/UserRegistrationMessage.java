package com.telus.codersplatform;

import lombok.*;

@Data
@Builder
public class UserRegistrationMessage {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String roles;
    private Long creationTimestamp;


}
