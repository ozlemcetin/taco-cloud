package tacos.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import tacos.entities.Ingredient;
import tacos.entities.User;
import tacos.repositories.IngredientRepository;
import tacos.repositories.UserRepository;


@Profile({"!prod", "!qa"})
@Configuration
public class DevelopmentConfig {

    /*
      For instance, you have a CommandLineRunner bean declared in TacoCloud-
      Application that’s used to load the embedded database with ingredient data when
      the application starts.

      That’s great for development, but would be unnecessary (and undesirable) in a production application.
      To prevent the ingredient data from being loaded every time the application starts in a production deployment,
      you could annotate the CommandLineRunner bean method with @Profile like this:

      Suppose that you need the CommandLineRunner created if either the dev profile
      or qa profile is active.In that case, you can list the profiles for which the bean
      should be created:

      @Profile({"dev", "qa"})

      It would be even more convenient if that CommandLineRunner bean were always created
      unless the prod profile is active. In that case, you can apply @Profile like this:

      @Profile("!prod")

      Here, the exclamation mark (!) negates the profile name. Effectively, it states that the
      CommandLineRunner bean will be created if the prod profile isn’t active.

      It’s also possible to use @Profile on an entire @Configuration-annotated class.
      For example, suppose that you were to extract the CommandLineRunner bean into a
      separate configuration class named DevelopmentConfig.

      Then you could annotate DevelopmentConfig with @Profile:

      @Profile({"!prod", "!qa"})

      Here, the CommandLineRunner bean (as well as any other beans defined in Development-
      Config) will only be created if neither the prod nor qa profiles are active
   */
    @Bean
    public CommandLineRunner dataLoader(IngredientRepository ingredientRepository,
                                        UserRepository userRepository, PasswordEncoder passwordEncoder) {

        return new CommandLineRunner() {

            @Override
            public void run(String... args) throws Exception {

                //ingredientRepository
                {
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
                }

                //userRepository
                {
                    User user = new User("habuma", passwordEncoder.encode("password"),
                            "Craig Walls", "123 North Street", "Cross Roads", "TX",
                            "76227", "123-123-1234");

                    userRepository.save(user);
                }

            }
        };

    }
}
