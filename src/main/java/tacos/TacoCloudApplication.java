package tacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TacoCloudApplication {

    /*
        Because you’ll be running the application from an executable JAR, it’s important to
        have a main class that will be executed when that JAR file is run. You’ll also need at
        least a minimal amount of Spring configuration to bootstrap the application.

        @SpringBootConfiguration—Designates this class as a configuration class.

        @EnableAutoConfiguration—Enables Spring Boot automatic configuration

        @ComponentScan—Enables component scanning. This lets you declare other
        classes with annotations like @Component, @Controller, and @Service to have
        Spring automatically discover and register them as components in the Spring
        application context.
     */

    public static void main(String[] args) {

        /*
            The main() method calls a static run() method on the SpringApplication class,
            which performs the actual bootstrapping of the application, creating the Spring application
            context.

            The two parameters passed to the run() method are a configuration
            class and the command-line arguments.
         */
        SpringApplication.run(TacoCloudApplication.class, args);
    }

}
