package tacos.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import tacos.domain.Ingredient;
import tacos.services.IngredientService;

@Component
public class IngredientByIdConverter implements Converter<String, Ingredient> {

    /*
        If the ingredients check boxes have textual (e.g., String) values,
        but the Taco object represents a list of ingredients as List<Ingredient>,
        then isn’t there a mismatch?

        How can a textual list like ["FLTO", "GRBF", "LETC"] be bound to
        a list of Ingredient objects that are richer objects containing not only an ID
        but also a descriptive name and ingredient type?

        That’s where a converter comes in handy.
        A converter is any class that implements Spring’s Converter interface
        and implements its convert() method to take one value and convert it to another.

        To convert a String to an Ingredient, we’ll use the IngredientByIdConverter as follows.
     */

    /*
        Notice that the IngredientByIdConverter is annotated with @Component to make
        it discoverable as a bean in the Spring application context. Spring Boot autoconfiguration
        will discover this, and any other Converter beans, and will automatically register
        them with Spring MVC to be used when the conversion of request parameters to
        bound properties is needed
     */

    private final IngredientService ingredientService;

    public IngredientByIdConverter(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }


    @Override
    public Ingredient convert(String source) {

        return ingredientService.findById(source);
    }
}
