package tacos.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import tacos.entities.Ingredient;
import tacos.entities.Order;
import tacos.entities.Taco;
import tacos.entities.User;
import tacos.repositories.IngredientRepository;
import tacos.repositories.TacoRepository;
import tacos.repositories.UserRepository;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/design")
@SessionAttributes("order")
public class DesignTacoController {


    private final IngredientRepository ingredientRepository;
    private final TacoRepository tacoRepository;
    private final UserRepository userRepository;


    @Autowired
    public DesignTacoController(IngredientRepository ingredientRepository, TacoRepository tacoRepository, UserRepository userRepository) {
        this.ingredientRepository = ingredientRepository;
        this.tacoRepository = tacoRepository;
        this.userRepository = userRepository;
    }

    /*
    As with the taco() method, the
    @ModelAttribute annotation on order() ensures that an Order object will be created
    in the model.

    But unlike the Taco object in the session, you need the order to be
    present across multiple requests so that you can create multiple tacos and add them
    to the order.

    The class-level @SessionAttributes annotation specifies any model
    objects like the order attribute that should be kept in session and available across
    multiple requests.
     */
    @ModelAttribute(name = "order")
    public Order order() {
        return new Order();
    }

    @ModelAttribute(name = "taco")
    public Taco taco() {
        return new Taco();
    }


    @GetMapping
    public String showDesignForm(Model model, Principal principal) {

        /*
        The findAll() method
        fetches all the ingredients from the database before filtering them into distinct types
        in the model.
         */
        List<Ingredient> ingredients = new ArrayList<>();
        ingredientRepository.findAll().forEach(el -> ingredients.add(el));


        Ingredient.Type[] types = Ingredient.Type.values();
        for (Ingredient.Type type : types) {
            model.addAttribute(type.toString().toLowerCase(Locale.US), filterByType(ingredients, type));
        }

        String username = principal.getName();
        User user = userRepository.findByUsername(username);
        model.addAttribute("user", user);

        return "design";
    }

    private static List<Ingredient> filterByType(List<Ingredient> ingredients, Ingredient.Type type) {
        return ingredients.stream()
                .filter(t -> t.getType().equals(type))
                .collect(Collectors.toList());
    }

    /*
    Now that you’ve declared how a Taco and Order should be validated, we need to
    revisit each of the controllers, specifying that validation should be performed when
    the forms are POSTed to their respective handler methods.

    To validate a submitted Taco, you need to add the Java Bean Validation API’s @Valid
    annotation to the Taco argument of DesignTacoController’s processDesign() method.
     */

    /*
    The Order parameter is annotated with @ModelAttribute to indicate that its
    value should come from the model and that Spring MVC shouldn’t attempt to bind
    request parameters to it.
     */
    @PostMapping
    public String processDesign(@Valid Taco taco, Errors errors, @ModelAttribute Order order) {

        if (errors.hasErrors()) {
            return "design";
        }

        // Save the taco design
        Taco savedTaco = tacoRepository.save(taco);

        /*
        adds the Taco object to the Order that’s kept in
        the session.
         */

        //addDesign
        order.addDesign(savedTaco);


        return "redirect:/orders/current";
    }
}
