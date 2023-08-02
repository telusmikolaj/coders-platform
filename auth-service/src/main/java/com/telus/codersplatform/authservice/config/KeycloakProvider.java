package com.telus.codersplatform.authservice.config;

import jakarta.annotation.PostConstruct;
import org.keycloak.admin.client.Keycloak;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KeycloakProvider {

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client}")
    private String client;

    @Value("${keycloak.admin.username}")
    private String adminUsername;

    @Value("${keycloak.admin.password}")
    private String adminPassword;


    private Keycloak adminInstance;

    @PostConstruct
    public void init() {
        this.adminInstance = Keycloak.getInstance(
                authServerUrl,
                realm,
                adminUsername,
                adminPassword,
                client);
    }

    public Keycloak getAdminInstance() {
        return this.adminInstance;
    }

    public Keycloak getUserInstance(String username, String password) {
        return Keycloak.getInstance(
                authServerUrl,
                realm,
                username,
                password,
                client);
    }
}
