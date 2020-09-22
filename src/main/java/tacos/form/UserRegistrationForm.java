package tacos.form;

import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;
import tacos.entities.User;

@Data
public class UserRegistrationForm {

    private String username;
    private String password;
    private String fullname;
    private String street;
    private String city;
    private String state;
    private String zip;
    private String phone;

    public User toUser(PasswordEncoder passwordEncoder) {

        /*
        to encode the password before saving it to the database.
         */
        return new User(username, passwordEncoder.encode(password), fullname,
                street, city, state, zip, phone);

    }
}
