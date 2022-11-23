package tacos.model;

import lombok.Data;

@Data
public class Ingredient {

    /*
        Perhaps the most unusual thing about the
        Ingredient class as defined in listing 2.1 is that it seems to be missing the usual set of
        getter and setter methods, not to mention useful methods like equals(), hashCode(),
        toString(), and others.

        You don’t see them in the listing partly to save space, but also because you’re using
        an amazing library called Lombok to automatically generate those methods at compile
        time so that they will be available at run time.

        In fact, the @Data annotation at the
        class level is provided by Lombok and tells Lombok to generate all of those missing
        methods as well as a constructor that accepts all final properties as arguments.
     */

    //create table if not exists Ingredient (
    //id varchar(4) not null,
    private final String id;

    //name varchar(25) not null,
    private final String name;

    //type varchar(10) not null
    private final Type type;

    public enum Type {
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }

}
