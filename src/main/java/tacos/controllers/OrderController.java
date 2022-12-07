package tacos.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import tacos.model.TacoOrder;
import tacos.repository.sd.jdbc.TacoOrderRepository;

import javax.validation.Valid;
import java.util.Date;

@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("tacoOrder")
public class OrderController {

    private final TacoOrderRepository tacoOrderRepository;

    public OrderController(TacoOrderRepository tacoOrderRepository) {
        this.tacoOrderRepository = tacoOrderRepository;
    }

    /*
        When combined with the method-level @GetMapping, it specifies that the orderForm() method will handle
        HTTP GET requests for /orders/current.
     */

    @GetMapping("/current")
    public String showOrderForm() {
        return "orderForm";
    }

    @PostMapping()
    public String processOrder(@Valid TacoOrder tacoOrder, Errors errors, SessionStatus sessionStatus) {

        if (errors.hasErrors()) {
            return "orderForm";
        }

        /*
            The TacoOrder object was initially created and placed into the session
            when the user created their first taco. By calling setComplete(), we are ensuring
            that the session is cleaned up and ready for a new order the next time the user creates
            a taco.
         */
        log.info("Order submitted: {}", tacoOrder);
        {
            tacoOrder.setPlacedAt(new Date());
            tacoOrderRepository.save(tacoOrder);
        }

        sessionStatus.setComplete();

        return "redirect:/";
    }
}
