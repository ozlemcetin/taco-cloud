package tacos.model;

import lombok.Data;
import org.hibernate.validator.constraints.CreditCardNumber;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class TacoOrder {

    /*
        When it comes to declaring validation on submitted taco orders, you must apply
        annotations to the TacoOrder class. For the address properties, you want to be sure
        that the user doesn’t leave any of the fields blank. For that, you’ll use the @NotBlank
        annotation.
     */

    /*
        When persisting objects to a database, it’s generally a good idea to have one field that
        uniquely identifies the object. Your Ingredient class already has an id field, but you
        need to add id fields to both Taco and TacoOrder as well.

        Moreover, it might be useful to know when a Taco is created and when a Taco-
        Order is placed. You’ll also need to add a field to each object to capture the date and
        time that the objects are saved. The following listing shows the new id and createdAt
        fields needed in the Taco class.
     */

    private Long id;

    private Date placedAt;

    //delivery information

    @NotBlank(message = "Delivery name is required")
    private String deliveryName;

    @NotBlank(message = "Street is required")
    private String deliveryStreet;

    @NotBlank(message = "City is required")
    private String deliveryCity;

    @NotBlank(message = "State is required")
    private String deliveryState;

    @NotBlank(message = "Zip code is required")
    private String deliveryZip;

    //payment information

    @CreditCardNumber(message = "Not a valid credit card number")
    private String ccNumber;

    @Pattern(regexp = "^(0[1-9]|1[0-2])([\\/])([2-9][0-9])$", message = "Must be formatted MM/YY")
    private String ccExpiration;

    @Digits(integer = 3, fraction = 0, message = "Invalid CVV")
    private String ccCVV;

    //the list of Taco objects that make up the order
    private List<Taco> tacos = new ArrayList<>();

    //=== methods
    public void addTaco(Taco taco) {
        this.tacos.add(taco);
    }
}
