package tacos.model;

import lombok.Data;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Table("TACO_ORDER")
public class TacoOrder implements Serializable {

    private static final long serialVersionUID = 1L;

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

    //create table if not exists Taco_Order
    //id identity,
    @Id
    @Column("ID")
    private Long id;

    //delivery information

    //delivery_Name varchar(50) not null,
    @Column("DELIVERY_NAME")
    @NotBlank(message = "Delivery name is required")
    private String deliveryName;

    //delivery_Street varchar(50) not null,
    @Column("DELIVERY_STREET")
    @NotBlank(message = "Street is required")
    private String deliveryStreet;

    //delivery_City varchar(50) not null,
    @Column("DELIVERY_CITY")
    @NotBlank(message = "City is required")
    private String deliveryCity;

    //delivery_State varchar(2) not null,
    @Column("DELIVERY_STATE")
    @NotBlank(message = "State is required")
    private String deliveryState;

    //delivery_Zip varchar(10) not null,
    @Column("DELIVERY_ZIP")
    @NotBlank(message = "Zip code is required")
    private String deliveryZip;

    //payment information

    //cc_number varchar(16) not null,
    @Column("CC_NUMBER")
    @CreditCardNumber(message = "Not a valid credit card number")
    private String ccNumber;

    //cc_expiration varchar(5) not null,
    @Column("CC_EXPIRATION")
    @Pattern(regexp = "^(0[1-9]|1[0-2])([\\/])([2-9][0-9])$", message = "Must be formatted MM/YY")
    private String ccExpiration;

    //cc_cvv varchar(3) not null,
    @Column("CC_CVV")
    @Digits(integer = 3, fraction = 0, message = "Invalid CVV")
    private String ccCVV;

    //placed_at timestamp not null
    @Column("PLACED_AT")
    private Date placedAt;

    //the list of Taco objects that make up the order
    private List<Taco> tacos = new ArrayList<>();

    //=== methods
    public void addTaco(Taco taco) {
        this.tacos.add(taco);
    }
}
