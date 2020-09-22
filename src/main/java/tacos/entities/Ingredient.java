package tacos.entities;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@Entity
public class Ingredient {


    /*
    In order to declare this as a JPA entity, Ingredient must be annotated with @Entity.
    And its id property must be annotated with @Id to designate it as the property that will
    uniquely identify the entity in the database.
     */


    /*
    In addition to the JPA-specific annotations, you’ll also note that you’ve added a
    @NoArgsConstructor annotation at the class level. JPA requires that entities have a noarguments
    constructor, so Lombok’s @NoArgsConstructor does that for you.

    You don’t want to be able to use it, though, so you make it private by setting the access
    attribute to AccessLevel.PRIVATE.

    And because there are final properties that must be set, you also set the force attribute to true,
    which results in the Lombok-generated constructor setting them to null.
     */

    /*
    You also add a @RequiredArgsConstructor. The @Data implicitly adds a required
    arguments constructor, but when a @NoArgsConstructor is used, that constructor gets
    removed. An explicit @RequiredArgsConstructor ensures that you’ll still have a
    required arguments constructor in addition to the private no-arguments constructor.
     */

    @Id
    private final String id;
    private final String name;
    private final Type type;

    public static enum Type {
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE

    }

    

}
 