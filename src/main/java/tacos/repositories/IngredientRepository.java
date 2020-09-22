package tacos.repositories;

import org.springframework.data.repository.CrudRepository;
import tacos.entities.Ingredient;


/*
    the good news about Spring Data JPA—there’s no need to write
    an implementation!

    When the application starts, Spring Data JPA automatically generates
    an implementation on the fly. This means the repositories are ready to use from
    the get-go.
 */
public interface IngredientRepository extends CrudRepository<Ingredient, String> {


}
