package tacos.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import tacos.domain.TacoOrder;

@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("tacoOrder")
public class OrderController {

    /*
        The class-level @RequestMapping specifies that any request-handling methods in
        this controller will handle requests whose path begins with /orders.

        When combined with the method-level @GetMapping,
        it specifies that the orderForm() method will handle HTTP GET requests for /orders/current.
     */
    @GetMapping("/current")
    public String orderForm() {

        return "orderForm";
    }

    /*
        When the processOrder() method is called to handle a submitted order, it’s given a
        TacoOrder object whose properties are bound to the submitted form fields.
     */

    @PostMapping
    public String processOrder(TacoOrder order, SessionStatus sessionStatus) {

        log.info("Order submitted: {}", order);

        /*
            But before processOrder() is done, it also calls setComplete()
            on the SessionStatus object passed in as a parameter.

            By calling setComplete(), we are ensuring that the session is cleaned up and
             ready for a new order the next time the user creates a taco.
         */
        sessionStatus.setComplete();

        return "redirect:/";
    }
}
