package tacos.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import tacos.model.Ingredient;
import tacos.model.Taco;
import tacos.model.TacoOrder;
import tacos.services.IngredientService;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("tacoOrder")
public class DesignTacoController {


    /*
        The first, @Slf4j, is a Lombok-provided annotation that, at compilation
        time, will automatically generate an SLF4J (Simple Logging Facade for Java,
        https://www.slf4j.org/) Logger static property in the class. This modest annotation
        has the same effect as if you were to explicitly add the following lines within the class:

        private static final org.slf4j.Logger log =
        org.slf4j.LoggerFactory.getLogger(DesignTacoController.class);

        DesignTacoController is @Controller. This
        annotation serves to identify this class as a controller and to mark it as a candidate for
        component scanning, so that Spring will discover it and automatically create an
        instance of DesignTacoController as a bean in the Spring application context.

        DesignTacoController is also annotated with @RequestMapping. The @Request-
        Mapping annotation, when applied at the class level, specifies the kind of requests that
        this controller handles. In this case, it specifies that DesignTacoController will handle
        requests whose path begins with /design.


        Finally, you see that DesignTacoController is annotated with @SessionAttributes
        ("tacoOrder"). This indicates that the TacoOrder object that is put into the model a
        little later in the class should be maintained in session.

        The creation of a taco is also the first step in creating an order, and the order we create will
        need to be carried in the session so that it can span multiple requests.
     */

    private final IngredientService ingredientService;

    public DesignTacoController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    /*
        Model is an object that ferries data between a
        controller and whatever view is charged with rendering that data. Ultimately, data
        that’s placed in Model attributes is copied into the servlet request attributes, where the
        view can find them and use them to render a page in the user’s browser
     */
    @ModelAttribute
    public void addIngredientsToModel(Model model) {

        Ingredient.Type[] types = Ingredient.Type.values();
        for (Ingredient.Type type : types) {
            model.addAttribute(type.toString(), ingredientService.filterByType(type));
        }
    }

    @ModelAttribute(name = "tacoOrder")
    public TacoOrder tacoOrder(Model model) {
        return new TacoOrder();
    }

    @ModelAttribute(name = "taco")
    public Taco taco(Model model) {
        return new Taco();
    }

    /*
        The class-level @RequestMapping specification is refined with the @GetMapping annotation
        that adorns the showDesignForm() method. @GetMapping, paired with the class level
        @RequestMapping, specifies that when an HTTP GET request is received for /design,
        Spring MVC will call showDesignForm() to handle the request.

        When showDesignForm() handles a GET request for /design, it doesn’t really do
        much. The main thing it does is return a String value of "design", which is the logical
        name of the view that will be used to render the model to the browser. But before
        it does that, it also populates the given Model with an empty Taco object under a key
        whose name is "taco". This will enable the form to have a blank slate on which to
        create a taco masterpiece.
     */

    @GetMapping
    public String showDesignForm() {
        return "design";
    }
}
