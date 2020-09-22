package tacos.controllers;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tacos.entities.User;
import tacos.form.UserRegistrationForm;
import tacos.repositories.UserRepository;

/*
@Controller to designate it as a controller and to mark it for component scanning.
It’s also annotated with @RequestMapping such that it will handle requests whose path
is /register.
 */

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public RegistrationController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String registerForm() {
        return "registration";
    }

    /*
    When the form is submitted, the HTTP POST request will be handled by the
    processRegistration() method.

    The RegistrationForm object given to processRegistration() is bound to the request data
     */

    @PostMapping
    public String processRegistration(UserRegistrationForm registrationForm) {

        /*
        When processing a form submission, RegistrationController passes it to the
        toUser() method, which uses it (PasswordEncoder) to encode the password before saving it to the database.

        In this way, the submitted password is written in an encoded form, and the user
        details service will be able to authenticate against that encoded password.
         */
        User user = registrationForm.toUser(passwordEncoder);
        userRepository.save(user);

        return "redirect:/login";
    }

}
