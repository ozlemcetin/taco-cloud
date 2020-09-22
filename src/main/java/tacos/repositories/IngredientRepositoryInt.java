package tacos.repositories;

import tacos.entities.Ingredient;

public interface IngredientRepositoryInt {

    Iterable<Ingredient> findAll();

    Ingredient findById(String id);

    Ingredient save(Ingredient ingredient);
}
