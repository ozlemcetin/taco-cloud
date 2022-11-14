package tacos.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TacoOrder {


    //delivery information
    private String deliveryName;


    private String deliveryStreet;


    private String deliveryCity;


    private String deliveryState;


    private String deliveryZip;

    //payment information
    private String ccNumber;

    private String ccExpiration;


    private String ccCVV;

    //the list of Taco objects that make up the order
    private List<Taco> tacos = new ArrayList<>();

    //=== methods
    public void addTaco(Taco taco) {
        this.tacos.add(taco);
    }
}
