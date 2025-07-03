package com.amigoscode;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;



//@SpringBootTest  /** Never use this for Unit test it slow down your test but might be ok for Integration Test**/
public class TestContainersTest extends AbstractTestContainers {


    @Test
    void canStartPostgresDB() {
        assertThat(postgreSQLContainer.isRunning()).isTrue();
        assertThat(postgreSQLContainer.isCreated()).isTrue();
    }


}
