package com.telus.codersplatform.userservice;

import lombok.Getter;

@Getter
public record UserRegistrationRequest(String email, String password) {

}
