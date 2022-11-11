package tacos.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Taco {

    private String name;

    private List<Ingredient> ingredients = new ArrayList<>();

}
