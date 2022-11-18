package tacos.repository;

import org.springframework.stereotype.Repository;
import tacos.model.Ingredient;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public class IngredientRepositoryHashMapImpl implements IngredientRepository {

    private final List<Ingredient> INGREDIENTS = Arrays.asList(new Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP),

            new Ingredient("COTO", "Corn Tortilla", Ingredient.Type.WRAP),

            new Ingredient("GRBF", "Ground Beef", Ingredient.Type.PROTEIN),

            new Ingredient("CARN", "Carnitas", Ingredient.Type.PROTEIN),

            new Ingredient("TMTO", "Diced Tomatoes", Ingredient.Type.VEGGIES),

            new Ingredient("LETC", "Lettuce", Ingredient.Type.VEGGIES),

            new Ingredient("CHED", "Cheddar", Ingredient.Type.CHEESE),

            new Ingredient("JACK", "Monterrey Jack", Ingredient.Type.CHEESE),

            new Ingredient("SLSA", "Salsa", Ingredient.Type.SAUCE),

            new Ingredient("SRCR", "Sour Cream", Ingredient.Type.SAUCE));

    @Override
    public List<Ingredient> findAll() {
        return INGREDIENTS;
    }


    @Override
    public Optional<Ingredient> findById(String id) {

        return INGREDIENTS.stream()

                .filter(x -> x.getId().equalsIgnoreCase(id)).findFirst();
    }

    @Override
    public Ingredient save(Ingredient ingredient) {
        return null;
    }
}
