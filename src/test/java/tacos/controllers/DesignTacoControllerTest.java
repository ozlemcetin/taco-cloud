package tacos.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tacos.services.IngredientService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DesignTacoController.class)
class DesignTacoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IngredientService ingredientService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void showDesignForm() throws Exception {

        //Performs GET /design
        mockMvc.perform(MockMvcRequestBuilders.get("/design"))

                //Expects HTTP 200
                .andExpect(MockMvcResultMatchers.status().isOk())

                //Expects view()
                .andExpect(MockMvcResultMatchers.view().name("design"))

                //Expects model() attributeExists
                .andExpect(MockMvcResultMatchers.model().attributeExists("WRAP"))

                .andExpect(MockMvcResultMatchers.model().attributeExists("PROTEIN"))

                .andExpect(MockMvcResultMatchers.model().attributeExists("VEGGIES"))

                .andExpect(MockMvcResultMatchers.model().attributeExists("CHEESE"))

                .andExpect(MockMvcResultMatchers.model().attributeExists("SAUCE"))

                //Expects  model() attributeExists
                .andExpect(MockMvcResultMatchers.model().attributeExists("taco"))

                .andExpect(MockMvcResultMatchers.model().attributeExists("tacoOrder"));
    }

    @Test
    void processTaco() throws Exception {

        //Performs POST /design
        mockMvc.perform(MockMvcRequestBuilders.post("/design")

                        .content("name=Test+Taco&ingredients=FLTO,GRBF,CHED")

                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))

                //Expects status()
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())

                //Expects header()
                .andExpect(MockMvcResultMatchers.header().stringValues("Location", "/orders/current"))

                //Expects view()
                .andExpect(MockMvcResultMatchers.view().name("redirect:/orders/current"));
    }
}