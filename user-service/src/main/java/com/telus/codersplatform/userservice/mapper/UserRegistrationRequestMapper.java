package com.telus.codersplatform.userservice.mapper;

import com.telus.codersplatform.userservice.model.User;
import com.telus.codersplatform.userservice.model.UserRegistrationRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserRegistrationRequestMapper {

    User registrationRequestToUser(UserRegistrationRequest request);

    UserRegistrationRequest userToRegistrationRequest(User user);

}
