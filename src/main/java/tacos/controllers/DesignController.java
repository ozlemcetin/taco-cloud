package tacos.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tacos.domain.Ingredient;
import tacos.domain.Taco;
import tacos.domain.TacoOrder;
import tacos.services.IngredientService;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("tacoOrder")
public class DesignController {

    /*
        @Slf4j, is a Lombok-provided annotation that, at compilation
        time, will automatically generate an SLF4J (Simple Logging Facade for Java)
     */

    // private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DesignTacoController.class);

    private final IngredientService ingredientService;

    public DesignController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    /*
        Model is an object that ferries data between a controller and
        whatever view is charged with rendering that data. Ultimately, data
        that’s placed in Model attributes is copied into the servlet request attributes,
        where the view can find them and use them to render a page in the user’s browser
     */
    @ModelAttribute
    public void addIngredientsToModel(Model model) {

        /*
            This method will also be invoked when a request is handled and
            will construct a list of Ingredient objects to be put into the model
         */

        Ingredient.Type[] types = Ingredient.Type.values();
        for (Ingredient.Type type : types) {

            /*
                A list of ingredient types is then added as an attribute to the Model object
                that will be passed into showDesignForm().
             */
            model.addAttribute(type.toString().toLowerCase(), ingredientService.filterByType(type));
        }
    }

    /*
        @SessionAttributes("tacoOrder").
        This indicates that the TacoOrder object that is put into the model a
        little later in the class should be maintained in session
        so that it can span multiple requests.
     */

    /*
        The TacoOrder object, referred to earlier in the @SessionAttributes annotation,
        holds state for the order being built as the user creates tacos across multiple requests
     */
    @ModelAttribute(name = "tacoOrder")
    public TacoOrder order() {
        return new TacoOrder();
    }

    /*
        The Taco object is placed into the model so that
        the view rendered in response to the GET request for /design
        will have a non-null object to display.
     */
    @ModelAttribute(name = "taco")
    public Taco taco() {
        return new Taco();
    }


    //@RequestMapping(method = RequestMethod.GET)
    @GetMapping
    public String showDesignForm() {

        /*
            The main thing it does is return a String value of "design", which is the logical
            name of the view that will be used to render the model to the browser.
            But before it does that, it also populates the given Model with an empty Taco object
            under a key whose name is "design".
         */

        //model.addAttribute("design", new Taco());

        return "design";
    }

    /*
        When the form is submitted, the fields in the form are bound to properties of a Taco object
     */

    /*
        The @ModelAttribute applied to the TacoOrder parameter indicates that
        it should use the TacoOrder object that was placed into the model
        via the @ModelAttribute-annotated order() method shown
     */
    @PostMapping
    public String processTaco(Taco taco, @ModelAttribute TacoOrder tacoOrder) {

        tacoOrder.addTaco(taco);

        log.info("Processing taco: {}", taco);

        /*
            the value returned from processTaco() is prefixed with "redirect:",
            indicating that this is a redirect view.
            More specifically, it indicates that after processTaco() completes,
            the user’s browser should be redirected to the relative path /orders/current.
         */
        return "redirect:/orders/current";
    }

}
