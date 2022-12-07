package tacos;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tacos.model.Ingredient;
import tacos.repository.sd.jdbc.IngredientRepository;

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

    /*
        Spring Boot offers two useful interfaces for executing logic when an application
        starts up: CommandLineRunner and ApplicationRunner. These two interfaces are quite
        similar. Both are functional interfaces that require that a single run() method be implemented.
        When the application starts up, any beans in the application context that
        implement CommandLineRunner or ApplicationRunner will have their run() methods
        invoked after the application context and all beans are wired up, but before anything
        else happens. This provides a convenient place for data to be loaded into the database.
     */

    /*
        The key difference between CommandLineRunner and ApplicationRunner is in the
        parameter passed to the respective run() methods. CommandLineRunner accepts a
        String vararg, which is a raw representation of arguments passed on the command
        line. But ApplicationRunner accepts an ApplicationArguments parameter that offers
        methods for accessing the arguments as parsed components of the command line
     */

    @Bean
    public CommandLineRunner dataLoader(IngredientRepository ingredientRepository) {
        return args -> {

            ingredientRepository.deleteAll();
            ingredientRepository.save(new Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP));
            ingredientRepository.save(new Ingredient("COTO", "Corn Tortilla", Ingredient.Type.WRAP));
            ingredientRepository.save(new Ingredient("GRBF", "Ground Beef", Ingredient.Type.PROTEIN));
            ingredientRepository.save(new Ingredient("CARN", "Carnitas", Ingredient.Type.PROTEIN));
            ingredientRepository.save(new Ingredient("TMTO", "Diced Tomatoes", Ingredient.Type.VEGGIES));
            ingredientRepository.save(new Ingredient("LETC", "Lettuce", Ingredient.Type.VEGGIES));
            ingredientRepository.save(new Ingredient("CHED", "Cheddar", Ingredient.Type.CHEESE));
            ingredientRepository.save(new Ingredient("JACK", "Monterrey Jack", Ingredient.Type.CHEESE));
            ingredientRepository.save(new Ingredient("SLSA", "Salsa", Ingredient.Type.SAUCE));
            ingredientRepository.save(new Ingredient("SRCR", "Sour Cream", Ingredient.Type.SAUCE));
        };
    }


}
