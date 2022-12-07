package tacos.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("INGREDIENT")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class Ingredient implements Persistable<String> {

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

    @Id
    @Column("ID")
    private String id;

    //name varchar(25) not null,
    @Column("NAME")
    private String name;

    //type varchar(10) not null
    @Column("TYPE")
    private Type type;

    @Override
    public boolean isNew() {
        return true;
    }

    public enum Type {
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }

}
