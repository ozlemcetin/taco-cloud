package tacos.repository.sd.jdbc;

import org.springframework.data.repository.CrudRepository;
import tacos.model.Ingredient;

public interface IngredientRepository extends CrudRepository<Ingredient, String> {

    /*
        Spring Data will automatically generate implementations for our repository interfaces
        at run time. But it will do that only for interfaces that extend one of the repository
        interfaces provided by Spring Data. At the very least, our repository interfaces will
        need to extend Repository so that Spring Data knows to create the implementation
        automatically.
     */


}
