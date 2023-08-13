package com.telus.codersplatform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleModel;
import org.keycloak.models.UserModel;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class CustomEventListenerProvider implements EventListenerProvider {

    private static final String QUEUE_NAME = "user_registration_queue";
    private final KeycloakSession session;
    private final ObjectMapper objectMapper;


    public CustomEventListenerProvider(KeycloakSession session) {
        this.session = session;
        this.objectMapper = new ObjectMapper();

    }

    @Override
    public void onEvent(Event event) {
        if (event.getType() == EventType.REGISTER) {
            RealmModel realm = session.getContext().getRealm();
            UserModel user = session.users().getUserById(realm, event.getUserId());
            UserRegistrationMessage userRegistrationMessage = getUserRegistrationMessage(user);
            sendUserRegistrationMessage(userRegistrationMessage);

            log.info("User registration:");
            log.info(userRegistrationMessage.toString());

        }
    }

    private void sendUserRegistrationMessage(UserRegistrationMessage message) {


        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("rabbitmq");
        factory.setUsername("admin");
        factory.setPassword("admin");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(QUEUE_NAME, true, false, false, null);

            String messageAsString = convertMessageToString(message);

            channel.basicPublish("", QUEUE_NAME, null, messageAsString.getBytes());
            log.info("Reg");
        } catch  (Exception e) {
            log.error("Cannot send message");
            e.printStackTrace();
        }
    }

    private UserRegistrationMessage getUserRegistrationMessage(UserModel user) {

        return UserRegistrationMessage.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .username(user.getUsername())
                .creationTimestamp(user.getCreatedTimestamp())
                .roles(getRolesJson(user))
                .build();
    }

    private String getRolesJson(UserModel user) {
        Stream<RoleModel> roleMappingsStream = user.getRoleMappingsStream();
        List<String> rolesList = roleMappingsStream.map(RoleModel::getName).collect(Collectors.toList());

        String rolesJson = "";
        try {
            rolesJson = this.objectMapper.writeValueAsString(rolesList);
        } catch (Exception e) {
            log.error("Cannot convert ROLES to JSON");
            e.printStackTrace();
            return null;
        }
        return rolesJson;
    }

    public  String convertMessageToString(UserRegistrationMessage userMessage) {
        try {
            return this.objectMapper.writeValueAsString(userMessage);
        } catch (Exception e) {
            log.error("Cannot convert message to JSON");
            throw new RuntimeException("Cannot convert message to JSON", e);
        }
    }



    @Override
    public void onEvent(AdminEvent adminEvent, boolean b) {

    }

    @Override
    public void close() {
    }
}
