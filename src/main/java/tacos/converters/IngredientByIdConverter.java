package tacos.converters;


import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import tacos.model.Ingredient;
import tacos.repository.IngredientRepository;

@Component
public class IngredientByIdConverter implements Converter<String, Ingredient> {

    /*
        Notice that the IngredientByIdConverter is annotated with @Component to make
        it discoverable as a bean in the Spring application context. Spring Boot autoconfiguration
        will discover this, and any other Converter beans, and will automatically register
        them with Spring MVC to be used when the conversion of request parameters to
        bound properties is needed.
     */

    private final IngredientRepository ingredientRepository;

    public IngredientByIdConverter(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }



    /*

        The ingredients check boxes also have textual
        values, but because zero or many of them may be selected, the ingredients property
        that they’re bound to is a List<Ingredient> that will capture each of the chosen
        ingredients.

        <!-- <input name="ingredients" type="checkbox" value="FLTO" /> -->

        If the ingredients check boxes have textual (e.g., String) values.

        How can a textual list like ["FLTO", "GRBF", "LETC"] be bound to a list of
        Ingredient?

        That’s where a converter comes in handy. A converter is any class that implements
        Spring’s Converter interface and implements its convert() method to take one
        value and convert it to another. To convert a String to an Ingredient
     */


    @Override
    public Ingredient convert(String source) {
        return ingredientRepository.findById(source).orElse(null);
    }
}
