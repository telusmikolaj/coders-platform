package com.telus.codersplatform.userservice;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class AbstractDaoUnitTest {


    @BeforeAll
    static void beforeAll() {
        Flyway flyway = Flyway.configure().dataSource(
                postgreSQLContainerProvider.getJdbcUrl(),
                postgreSQLContainerProvider.getUsername(),
                postgreSQLContainerProvider.getPassword()
        ).load();
        flyway.migrate();
    }

    @Container
    protected static final PostgreSQLContainer<?> postgreSQLContainerProvider =
            new PostgreSQLContainer<>("postgres:latest")
                    .withDatabaseName("dao-unit-test")
                    .withUsername("telusmikolaj")
                    .withPassword("admin");

    @DynamicPropertySource
    private static void registerDataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add(
                "spring.datasource.url",
                postgreSQLContainerProvider::getJdbcUrl
        );
        registry.add(
                "spring.datasource.username",
                postgreSQLContainerProvider::getUsername
        );
        registry.add(
                "spring.datasource.password",
                postgreSQLContainerProvider::getPassword
        );

    }

}
