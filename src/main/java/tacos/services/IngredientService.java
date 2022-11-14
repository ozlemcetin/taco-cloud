package tacos.services;

import org.springframework.stereotype.Service;
import tacos.model.Ingredient;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngredientService {

    public List<Ingredient> findAll() {

        List<Ingredient> ingredients = Arrays.asList(new Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP),

                new Ingredient("COTO", "Corn Tortilla", Ingredient.Type.WRAP),

                new Ingredient("GRBF", "Ground Beef", Ingredient.Type.PROTEIN),

                new Ingredient("CARN", "Carnitas", Ingredient.Type.PROTEIN),

                new Ingredient("TMTO", "Diced Tomatoes", Ingredient.Type.VEGGIES),

                new Ingredient("LETC", "Lettuce", Ingredient.Type.VEGGIES),

                new Ingredient("CHED", "Cheddar", Ingredient.Type.CHEESE),

                new Ingredient("JACK", "Monterrey Jack", Ingredient.Type.CHEESE),

                new Ingredient("SLSA", "Salsa", Ingredient.Type.SAUCE),

                new Ingredient("SRCR", "Sour Cream", Ingredient.Type.SAUCE));

        return ingredients;
    }

    public List<Ingredient> filterByType(Ingredient.Type type) {

        List<Ingredient> ingredients = findAll();

        return ingredients.stream().filter(x -> x.getType().equals(type)).collect(Collectors.toList());

    }

    public Ingredient findById(String id) {

        if (id != null && !id.isEmpty()) {

            List<Ingredient> ingredients = findAll();

            return ingredients.stream().filter(x -> x.getId().equalsIgnoreCase(id)).findFirst().orElse(null);
        }

        return null;
    }
}
