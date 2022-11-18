package tacos.repository;

import tacos.model.Ingredient;

import java.util.Optional;

public interface IngredientRepository {

    /*
        Your Ingredient repository needs to perform the following operations:
         Query for all ingredients into a collection of Ingredient objects
         Query for a single Ingredient by its id
         Save an Ingredient object
     */
    Iterable<Ingredient> findAll();

    Optional<Ingredient> findById(String id);

    Ingredient save(Ingredient ingredient);


}
