package tacos.controllers;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tacos.entities.Ingredient;
import tacos.entities.Taco;
import tacos.repositories.IngredientRepository;
import tacos.repositories.TacoRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(DesignTacoController.class)
public class DesignTacoControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IngredientRepository ingredientRepository;

    @MockBean
    private TacoRepository tacoRepository;

    Ingredient.Type wrapType = Ingredient.Type.WRAP;
    private final String fltoId = "FLTO";
    private final Ingredient fltoIngredient = new Ingredient(fltoId, "Flour Tortilla", wrapType);

    private final String grbfId = "GRBF";
    private final Ingredient grbfIngredient = new Ingredient(grbfId, "Ground Beef", Ingredient.Type.PROTEIN);

    private final String chedId = "CHED";
    private final Ingredient chedIngredient = new Ingredient(chedId, "Cheddar", Ingredient.Type.CHEESE);

    private List<Ingredient> ingredients;
    private Taco design;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void test_showDesignForm() throws Exception {

        {
            ingredients = Arrays.asList(
                    fltoIngredient,
                    grbfIngredient,
                    chedIngredient);

            //findAll
            when(ingredientRepository.findAll()).thenReturn(ingredients);

        }

        mockMvc.perform(MockMvcRequestBuilders.get("/design"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("design"))
                .andExpect(MockMvcResultMatchers.model().attribute(wrapType.toString().toLowerCase(Locale.US), ingredients.subList(0, 1)));
    }

    @Test
    public void test_processDesign() throws Exception {

        {
             /*
            errors = [Field error in object 'taco' on field 'ingredients': rejected value [FLTO,GRBF,CHED];
            codes [typeMismatch.taco.ingredients,typeMismatch.ingredients,typeMismatch.java.util.List,typeMismatch];
            arguments [org.springframework.context.support.DefaultMessageSourceResolvable:
            codes [taco.ingredients,ingredients]; arguments []; default message [ingredients]];
            default message [Failed to convert property value of type 'java.lang.String' to required type 'java.util.List'
            for property 'ingredients'; nested exception is java.lang.IllegalStateException:
            Cannot convert value of type 'java.lang.String' to required type 'tacos.beans.Ingredient' for property 'ingredients[0]':
            no matching editors or conversion strategy found]]
             */

            /*
            used in public class IngredientByIdConverter implements Converter<String, Ingredient> {
             */

            //findById
            when(ingredientRepository.findById(fltoId)).thenReturn(Optional.of(fltoIngredient));
            when(ingredientRepository.findById(grbfId)).thenReturn(Optional.of(grbfIngredient));
            when(ingredientRepository.findById(chedId)).thenReturn(Optional.of(chedIngredient));
        }

        {

            ingredients = Arrays.asList(
                    fltoIngredient,
                    grbfIngredient,
                    chedIngredient);

            design = new Taco();
            design.setName("Test Taco");
            design.setIngredients(ingredients);

            //save
            when(tacoRepository.save(design))
                    .thenReturn(design);

        }


        mockMvc.perform(MockMvcRequestBuilders.post("/design")
                .content("name=Test+Taco&ingredients=" + fltoId + "," + grbfId + "," + chedId)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/orders/current"))
                .andExpect(MockMvcResultMatchers.header().stringValues("Location", "/orders/current"));
    }

}