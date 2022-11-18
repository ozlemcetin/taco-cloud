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

    public List<Ingredient> findAll() {
        return ingredientRepository.findAll();
    }

    public List<Ingredient> filterByType(Ingredient.Type type) {
        return ingredientRepository.filterByType(type);
    }

    public Ingredient findById(String id) {
        return ingredientRepository.findById(id).orElse(null);
    }
}
