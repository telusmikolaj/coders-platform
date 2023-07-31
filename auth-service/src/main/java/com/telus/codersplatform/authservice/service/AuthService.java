package com.telus.codersplatform.authservice.service;

import com.telus.codersplatform.authservice.model.UserLoginRequest;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client}")
    private String client;

    public String loginUser(UserLoginRequest loginRequest) {
        Keycloak keycloak = Keycloak.getInstance(
                authServerUrl,
                realm,
                loginRequest.email(),
                loginRequest.password(),
                client);

        AccessTokenResponse tokenResponse = keycloak.tokenManager().getAccessToken();

        return tokenResponse.getToken();
    }
}
