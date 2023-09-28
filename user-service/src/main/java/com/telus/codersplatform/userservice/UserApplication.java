package com.telus.codersplatform.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = {
                "com.telus.codersplatform.ampq",
                "com.telus.codersplatform.userservice",

        }
)
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);


    }

}
