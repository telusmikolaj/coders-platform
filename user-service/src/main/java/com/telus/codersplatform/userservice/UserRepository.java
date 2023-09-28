package com.telus.codersplatform.userservice;

import com.telus.codersplatform.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

