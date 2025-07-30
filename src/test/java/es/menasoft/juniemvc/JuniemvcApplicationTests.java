package es.menasoft.juniemvc;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * Main application test to verify that the application context loads correctly.
 */
@SpringBootTest
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.flyway.enabled=false"
})
class JuniemvcApplicationTests {

    @Test
    void contextLoads() {
        // This test verifies that the application context loads successfully
    }

}
