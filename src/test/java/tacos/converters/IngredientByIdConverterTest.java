package tacos.converters;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import tacos.model.Ingredient;
import tacos.services.IngredientService;

class IngredientByIdConverterTest {

    private IngredientByIdConverter converter;
    private IngredientService ingredientService;

    @BeforeEach
    void setUp() {

        //mock
        ingredientService = Mockito.mock(IngredientService.class);
        converter = new IngredientByIdConverter(ingredientService);
    }

    @Test
    void convert_shouldReturnValueWhenPresent() {

        String id = "AAAA";
        Ingredient ingredient = null;
        {
            ingredient = new Ingredient(id, "TEST INGREDIENT", Ingredient.Type.CHEESE);

            //when
            Mockito.when(ingredientService.findById(id)).thenReturn(ingredient);
        }

        Ingredient convertedIngredient = converter.convert(id);
        Assertions.assertThat(convertedIngredient.equals(ingredient));
    }

    @Test
    void convert_shouldReturnNullWhenMissing() {

        String id = "AAAA";
        {
            //when
            Mockito.when(ingredientService.findById(id)).thenReturn(null);
        }

        Ingredient convertedIngredient = converter.convert(id);
        Assertions.assertThat(convertedIngredient == null);
    }
}