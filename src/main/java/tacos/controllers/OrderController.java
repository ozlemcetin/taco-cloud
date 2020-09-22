package tacos.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import tacos.configurationproperties.OrderProps;
import tacos.entities.Order;
import tacos.entities.User;
import tacos.repositories.OrderRepository;

import javax.validation.Valid;


@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("order")
public class OrderController {

    private final OrderRepository orderRepository;

    /*
        There’s nothing particularly special about configuration property holders. They’re
        beans that have their properties injected from the Spring environment. They can be
        injected into any other bean that needs those properties
     */
    private final OrderProps orderProps;

    public OrderController(OrderRepository orderRepository, OrderProps orderProps) {
        this.orderRepository = orderRepository;
        this.orderProps = orderProps;
    }

    @GetMapping("/current")
        public String orderForm(@AuthenticationPrincipal User user, @ModelAttribute Order order) {

        if (order.getDeliveryName() == null) {
            order.setDeliveryName(user.getFullname());
        }
        if (order.getDeliveryStreet() == null) {
            order.setDeliveryStreet(user.getStreet());
        }
        if (order.getDeliveryCity() == null) {
            order.setDeliveryCity(user.getCity());
        }
        if (order.getDeliveryState() == null) {
            order.setDeliveryState(user.getState());
        }
        if (order.getDeliveryZip() == null) {
            order.setDeliveryZip(user.getZip());
        }

        return "orderForm";
    }

    /*
    Here, the Order object submitted
    in the form (which also happens to be the same Order object maintained in
    session) is saved via the save() method on the injected OrderRepository
     */

    /*
    There are several ways to determine who the user is. These are a few of the most
    common ways:

    A) Inject a Principal object into the controller method:

        public String processOrder(@Valid Order order, Errors errors, SessionStatus sessionStatus,
        Principal principal){...}

        User user = userRepository.findByUsername(principal.getName());
        order.setUser(user);

    B) Inject an Authentication object into the controller method

        public String processOrder(@Valid Order order, Errors errors, SessionStatus sessionStatus,
        Authentication authentication) {...}

        User user = (User)  authentication.getPrincipal();
        order.setUser(user);

    C) Use SecurityContextHolder to get at the security context

        Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();
        order.setUser(user);

        Although this snippet is thick with security-specific code, it has one advantage over the
        other approaches described: it can be used anywhere in the application, not only in a
        controller’s handler methods. This makes it suitable for use in lower levels of the code.


    D) Use an @AuthenticationPrincipal annotated method


        public String processOrder(@Valid Order order, Errors errors,SessionStatus sessionStatus,
        @AuthenticationPrincipal User user) {...}

        order.setUser(user);

     */
    @PostMapping
    public String processOrder(@Valid Order order, Errors errors, SessionStatus sessionStatus,
                               @AuthenticationPrincipal User user) {

        if (errors.hasErrors()) {
            return "orderForm";
        }

        //setUser
        order.setUser(user);

        //save
        log.info("Order submitted:  " + order);
        orderRepository.save(order);

        //setComplete
        sessionStatus.setComplete();

        return "redirect:/";
    }

    @GetMapping
    public String ordersForUser(@AuthenticationPrincipal User user, Model model) {

        /*
        A few orders displayed in the browser are useful; a never-ending list of hundreds
        of orders is just noise. Let’s say that you want to limit the number of orders displayed
        to the most recent 20 orders.

        Pageable pageable = PageRequest.of(0, 20);

        along with the corresponding changes to OrderRepository:

        List<Order> findByUserOrderByPlacedAtDesc(User user, Pageable pageable);

        you constructed a PageRequest object that implemented Pageable to
        request the first page (page zero) with a page size of 20 to get up to 20 of the most
        recently placed orders for the user.
         */
        Pageable pageable = PageRequest.of(0, orderProps.getPageSize());

        model.addAttribute("orders", orderRepository.findByUserOrderByPlacedAtDesc(user, pageable));

        return "orderList";
    }
}
