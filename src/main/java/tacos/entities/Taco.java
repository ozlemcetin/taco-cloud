package tacos.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Taco {

    /*
    the Taco class is now annotated with @Entity and has its id property
    annotated with @Id.

    Because you’re relying on the database to automatically
    generate the ID value, you also annotate the id property with @GeneratedValue, specifying
    a strategy of AUTO.
     */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date createdAt;

    @NotNull
    @Size(min = 5, message = "Name must be at least 5 characters long")
    private String name;

    /*
    To declare the relationship between a Taco and its associated Ingredient list, you
    annotate ingredients with @ManyToMany. A Taco can have many Ingredient objects,
    and an Ingredient can be a part of many Tacos.
     */

    @ManyToMany(targetEntity = Ingredient.class)
    @Size(min = 1, message = "You must choose at least 1 ingredient")
    private List<Ingredient> ingredients;

    /*
    You’ll also notice that there’s a new method, createdAt(), which is annotated with
    @PrePersist. You’ll use this to set the createdAt property to the current date and
    time before Taco is persisted
     */

    @PrePersist
    void createdAt() {
        this.createdAt = new Date();
    }
}
