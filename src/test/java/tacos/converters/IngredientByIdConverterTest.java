package tacos.converters;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import tacos.model.Ingredient;
import tacos.repository.IngredientRepository;

import java.util.Optional;

class IngredientByIdConverterTest {

    private IngredientByIdConverter converter;
    private IngredientRepository ingredientRepository;

    @BeforeEach
    void setUp() {

        //mock
        ingredientRepository = Mockito.mock(IngredientRepository.class);
        converter = new IngredientByIdConverter(ingredientRepository);
    }

    @Test
    void convert_shouldReturnValueWhenPresent() {

        String id = "AAAA";
        Ingredient ingredient = null;
        {
            ingredient = new Ingredient(id, "TEST INGREDIENT", Ingredient.Type.CHEESE);

            //when
            Mockito.when(ingredientRepository.findById(id)).thenReturn(Optional.of(ingredient));
        }

        Ingredient convertedIngredient = converter.convert(id);
        Assertions.assertThat(convertedIngredient.equals(ingredient));
    }

    @Test
    void convert_shouldReturnNullWhenMissing() {

        String id = "AAAA";
        {
            //when
            Mockito.when(ingredientRepository.findById(id)).thenReturn(Optional.empty());
        }

        Ingredient convertedIngredient = converter.convert(id);
        Assertions.assertThat(convertedIngredient == null);
    }
}