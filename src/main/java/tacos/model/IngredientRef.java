package tacos.model;

import lombok.Data;

@Data
public class IngredientRef {

   /*
        create table if not exists Ingredient_Ref (
            ingredient varchar(4) not null,
            taco bigint not null,
            taco_key bigint not null
        );

        alter table Ingredient_Ref
            add foreign key (ingredient) references Ingredient(id);
    */

    /*
        When you save a TacoOrder, you also must
        save the Taco objects that go with it. And when you save the Taco objects, you’ll also
        need to save an object that represents the link between the Taco and each Ingredient
        that makes up the taco. The IngredientRef class defines that linking between Taco
        and Ingredient as follows:
     */
    private final String ingredient;
}
