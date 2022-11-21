package tacos.services;

import org.springframework.stereotype.Service;
import tacos.model.Ingredient;
import tacos.repository.IngredientRepository;

import java.util.List;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public Iterable<Ingredient> findAll() {
        return ingredientRepository.findAll();
    }


    public Ingredient findById(String id) {
        return ingredientRepository.findById(id).orElse(null);
    }


    public Ingredient save(Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }

    public List<Ingredient> filterByType(Ingredient.Type type) {
        return ingredientRepository.filterByType(type);
    }
}
