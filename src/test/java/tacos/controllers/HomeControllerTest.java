package tacos.controllers;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tacos.repository.IngredientRepository;
import tacos.repository.TacoOrderRepository;


//@WebMvcTest(HomeController.class)
@WebMvcTest
public class HomeControllerTest {

    /*
        @WebMvcTest(HomeController.class) Web test for HomeController

        This is a special test annotation provided by Spring Boot that arranges for the test to run in
        the context of a Spring MVC application. More specifically, in this case, it arranges for
        HomeController to be registered in Spring MVC so that you can send requests to it.

        @Autowired Injects MockMvc
     */
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IngredientRepository ingredientRepository;

    @MockBean
    private TacoOrderRepository tacoOrderRepository;

    @Test
    public void testHomePage() throws Exception {

        //Performs GET /
        mockMvc.perform(MockMvcRequestBuilders.get("/"))

                //Expects HTTP 200
                .andExpect(MockMvcResultMatchers.status().isOk())

                //Expects home view
                .andExpect(MockMvcResultMatchers.view().name("home"))

                //Expects Welcome to…
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Welcome to...")));
    }

}
