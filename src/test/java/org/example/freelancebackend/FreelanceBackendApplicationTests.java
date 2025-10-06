package org.example.freelancebackend;

import org.junit.jupiter.api.Test;
import org.matvey.freelancebackend.FreelanceBackendApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = FreelanceBackendApplication.class)
@ActiveProfiles("test")
class FreelanceBackendApplicationTests {

    @Test
    void contextLoads() {
    }

}
