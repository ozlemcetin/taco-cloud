package tacos;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TacoCloudApplicationTests {

    /*
        Even so, this test class does perform an essential check to ensure
        that the Spring application context can be loaded successfully. If you make any
        changes that prevent the Spring application context from being created, this test fails,
        and you can react by fixing the problem.
     */

    /*
        The @SpringBootTest annotation tells JUnit to bootstrap the test with Spring Boot
        capabilities.

        Just like @SpringBootApplication, @SpringBootTest is a composite annotation,
        which is itself annotated with @ExtendWith(SpringExtension.class),
        to add Spring testing capabilities to JUnit 5
     */
    @Test
    void contextLoads() {
    }

}
