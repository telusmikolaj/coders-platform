package com.telus.codersplatform.authservice.service;

import com.telus.codersplatform.authservice.config.ErrorMessages;
import com.telus.codersplatform.authservice.config.KeycloakProvider;
import com.telus.codersplatform.authservice.model.UserLoginRequest;
import com.telus.codersplatform.authservice.model.UserRegistrationRequest;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AuthService {

    final List<String> USER_ROLES = Collections.singletonList("user");
    private final KeycloakProvider keycloakProvider;
    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.client}")
    private String client;

    public AuthService(KeycloakProvider keycloakProvider) {
        this.keycloakProvider = keycloakProvider;
    }

    public ResponseEntity<?> createUser(UserRegistrationRequest registrationRequest) {
        try {
            log.info("Creating user with email:  {}", registrationRequest.getEmail());

            Keycloak keycloak = keycloakProvider.getAdminInstance();
            RealmResource realmResource = keycloak.realm(realm);
            UsersResource usersResource = realmResource.users();

            UserRepresentation user = createUserRepresentation(registrationRequest);

            return handleCreateUserResponse(usersResource, user);
        } catch (RuntimeException e) {
            e.printStackTrace();
            log.error("Error while creating user {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            log.error("Error while creating user {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while creating the user");
        }
    }

    private ResponseEntity<?> handleCreateUserResponse(UsersResource usersResource, UserRepresentation user) {
        try (Response response = usersResource.create(user)) {
            ResponseEntity<?> responseEntity = handleResponseStatus(response);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                String userId = CreatedResponseUtil.getCreatedId(response);
                sendVerificationMail(usersResource, userId);
                assignUserRoles(usersResource, userId);
                return ResponseEntity.ok().build();
            } else {
                return responseEntity;
            }
        }
    }

    private void sendVerificationMail(UsersResource usersResource, String userId) {
        usersResource.get(userId).executeActionsEmail(List.of("VERIFY_EMAIL"));
    }


    private ResponseEntity<?> handleResponseStatus(Response response) {
        int status = response.getStatus();
        if (status == 400) {
            log.error("Error while creating user {}", "Bad request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessages.BAD_REQUEST);
        } else if (status == 401) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorMessages.UNAUTHORIZED);
        } else if (status == 403) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorMessages.FORBIDDEN);
        } else if (status == 409) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorMessages.CONFLICT);
        } else if (status == 500) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessages.INTERNAL_SERVER_ERROR);
        } else if (status != 201) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessages.UNEXPECTED_ERROR);
        }

        return ResponseEntity.ok().build();
    }


    public ResponseEntity<?> loginUser(UserLoginRequest loginRequest) {
        try {
            Keycloak keycloak = keycloakProvider.getUserInstance(loginRequest.email(), loginRequest.password());
            AccessTokenResponse tokenResponse = keycloak.tokenManager().getAccessToken();
            return ResponseEntity.ok(tokenResponse.getToken());

        } catch (BadRequestException e) {
            log.error("Email has not been verified {}", loginRequest.email());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessages.EMAIL_NOT_VERIFIED);
        } catch (NotAuthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorMessages.INVALID_CREDENTIALS);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessages.INTERNAL_SERVER_ERROR);
        }
    }

    private UserRepresentation createUserRepresentation(UserRegistrationRequest registrationRequest) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(registrationRequest.getEmail());
        user.setEmail(registrationRequest.getEmail());
        user.setEnabled(true);

        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(registrationRequest.getPassword());
        user.setCredentials(List.of(passwordCred));

        return user;
    }

    private void assignUserRoles(UsersResource usersResource, String userId) {
        Keycloak keycloak = keycloakProvider.getAdminInstance();

        ClientsResource clients = keycloak.realm(realm).clients();
        if (clients == null) {
            throw new IllegalStateException("Client not found");
        }

        List<ClientRepresentation> clientRepresentations = clients.findByClientId(client);

        if (clientRepresentations == null || clientRepresentations.isEmpty()) {
            throw new IllegalStateException("Client not found");
        }
        String clientId = Optional.ofNullable(clientRepresentations.get(0).getId())
                .orElseThrow(() -> new IllegalStateException("Client id is null"));

        List<RoleRepresentation> allRoles = clients.get(clientId).roles().list();

        List<RoleRepresentation> rolesToAssign = allRoles.stream()
                .filter(role -> USER_ROLES.contains(role.getName()))
                .toList();

        if (rolesToAssign.isEmpty()) {
            throw new RuntimeException("None of the roles were found");
        }

        try {

            usersResource.get(userId).roles().clientLevel(clientId).add(rolesToAssign);

        } catch (NotFoundException e) {
            throw new RuntimeException("Role or client level not found: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while assigning roles: " + e.getMessage());
        }
    }
}
