package tacos.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class Taco {

    /*
        One way to perform form validation is to litter the processTaco() and process-
        Order() methods with a bunch of if/then blocks, checking each and every field to
        ensure that it meets the appropriate validation rules. But that would be cumbersome
        and difficult to read and debug.

        Fortunately, Spring supports the JavaBean Validation API (also known as JSR 303;
        https://jcp.org/en/jsr/detail?id=303). This makes it easy to declare validation rules as
        opposed to explicitly writing declaration logic in your application code.
        To apply validation in Spring MVC, you need to

        Add the Spring Validation starter to the build.

        Declare validation rules on the class that is to be validated: specifically, the
        Taco class.

        Specify that validation should be performed in the controller methods that
        require validation: specifically, the DesignTacoController’s processTaco()
        method and the OrderController’s processOrder() method.

        Modify the form views to display validation errors
     */

    /*
        The Validation API offers several annotations that can be placed on properties of
        domain objects to declare validation rules. Hibernate’s implementation of the Validation
        API adds even more validation annotations. Both can be added to a project by
        adding the Spring Validation starter to the build
     */

    /*
        For the Taco class, you want to ensure that the name property isn’t empty or null and
        that the list of selected ingredients has at least one item. The following listing shows
        an updated Taco class that uses @NotNull and @Size to declare those validation rules

        You’ll notice that in addition to requiring that the name property isn’t null, you
        declare that it should have a value that’s at least five characters in length.
     */

    //create table if not exists Taco (
    //id identity,
    private Long id;

    //name varchar(50) not null,
    @NotNull
    @Size(min = 5, message = "Name must be at least 5 characters long")
    private String name;

    //alter table Taco
    //add foreign key (taco_order) references Taco_Order(id);
    //taco_order bigint not null,

    //taco_order_key bigint not null,

    //created_at timestamp not null
    private Date createdAt = new Date();

    @NotNull
    @Size(min = 1, message = "You must choose at least 1 ingredient")
    private List<IngredientRef> ingredients = new ArrayList<>();

    //=== methods
    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(new IngredientRef(ingredient.getId()));
    }

}
