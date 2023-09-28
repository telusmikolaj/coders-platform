package com.telus.codersplatform.userservice;

import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
class TestcontainersTest extends AbstractDaoUnitTest {
    @Test
    void canStartPostgres() {
        assertThat(postgreSQLContainerProvider.isRunning()).isTrue();
        assertThat(postgreSQLContainerProvider.isCreated()).isTrue();
    }


}
