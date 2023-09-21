package com.telus.codersplatform.userservice.model;

import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class UserRegistrationRequest {
    private String id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String roles;
    private Long creationTimestamp;




}
