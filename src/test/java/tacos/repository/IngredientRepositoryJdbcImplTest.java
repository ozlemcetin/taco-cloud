package tacos.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import tacos.model.Ingredient;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class IngredientRepositoryJdbcImplTest {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
    }

    @Test
    void findAll() {
    }

    @Test
    void findById() {

        Optional<Ingredient> fetchedFLTO = ingredientRepository.findById("FLTO");
        assertThat(fetchedFLTO.isPresent()).isTrue();
        assertThat(fetchedFLTO.get()).isEqualTo(new Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP));
    }

    @Test
    void findById_NotFound() {

        Optional<Ingredient> fetchedXXXX = ingredientRepository.findById("XXXX");
        assertThat(fetchedXXXX.isEmpty()).isTrue();
    }

    @Test
    void save() {
    }

    @Test
    void filterByType() {
    }
}