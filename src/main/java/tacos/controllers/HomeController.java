package tacos.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    /*
        A controller class that handles requests for the root path (for example, /)
        --for the home page.

        if an HTTP GET request is received for the root path /,
        then this method should handle that request.

        Spring comes with a powerful web framework known as Spring MVC. At the center of
        Spring MVC is the concept of a controller, a class that handles requests and responds
        with information of some sort. In the case of a browser-facing application, a controller
        responds by optionally populating model data and passing the request on to a view to
        produce HTML that’s returned to the browser.
     */

    @GetMapping("/")
    public String home() {

        /*
            forwards the request to the home page view without populating any model data

            The template name is derived from the logical view name by prefixing it with /templates/
            and postfixing it with .html.
         */
        return "home";

    }
}