package tacos.services;

import org.springframework.stereotype.Service;
import tacos.domain.Ingredient;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IngredientService {

    private static final List<Ingredient> ingredients = Arrays.asList(new Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP),

            new Ingredient("COTO", "Corn Tortilla", Ingredient.Type.WRAP),

            new Ingredient("GRBF", "Ground Beef", Ingredient.Type.PROTEIN),

            new Ingredient("CARN", "Carnitas", Ingredient.Type.PROTEIN),

            new Ingredient("TMTO", "Diced Tomatoes", Ingredient.Type.VEGGIES),

            new Ingredient("LETC", "Lettuce", Ingredient.Type.VEGGIES),

            new Ingredient("CHED", "Cheddar", Ingredient.Type.CHEESE),

            new Ingredient("JACK", "Monterrey Jack", Ingredient.Type.CHEESE),

            new Ingredient("SLSA", "Salsa", Ingredient.Type.SAUCE),

            new Ingredient("SRCR", "Sour Cream", Ingredient.Type.SAUCE));

    public List<Ingredient> findAll() {
        return ingredients;
    }

    public Ingredient findById(String id) {

        Optional<Ingredient> ingredientOptional = ingredients.stream()

                .filter(x -> x.getId().equals(id))

                .findFirst();

        return ingredientOptional.orElse(null);
    }

    public List<Ingredient> filterByType(Ingredient.Type type) {

        return ingredients.stream()

                .filter(x -> x.getType().equals(type))

                .collect(Collectors.toList());
    }


}
