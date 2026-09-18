package org.example.pensionatkademina;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
class PensionatKademinaApplicationTests {

    @Container
    @ServiceConnection
    static MySQLContainer db =
            new MySQLContainer(
                    "mysql:8");

    @Test
    void contextLoads() {
    }
}
