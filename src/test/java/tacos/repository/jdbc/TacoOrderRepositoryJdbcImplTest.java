package tacos.repository.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.test.annotation.DirtiesContext;
import tacos.model.Taco;
import tacos.model.TacoOrder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TacoOrderRepositoryJdbcImplTest {

    @Autowired
    private TacoOrderJdbcRepository jdbcRepository;

    @Autowired
    private JdbcOperations jdbcOperations;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void save() {

        Taco taco1 = null;
        {
            taco1 = new Taco();
            taco1.setName("Taco One");
            taco1.addIngredientRef("FLTO");
            taco1.addIngredientRef("GRBF");
            taco1.addIngredientRef("CHED");
        }

        Taco taco2 = null;
        {
            taco2 = new Taco();
            taco2.setName("Taco Two");
            taco2.addIngredientRef("COTO");
            taco2.addIngredientRef("CARN");
            taco2.addIngredientRef("JACK");
        }

        TacoOrder order = new TacoOrder();
        {
            //id
            order.setDeliveryName("Test McTest");
            order.setDeliveryStreet("1234 Test Lane");
            order.setDeliveryCity("Testville");
            order.setDeliveryState("CO");
            order.setDeliveryZip("80123");
            order.setCcNumber("4111111111111111");
            order.setCcExpiration("10/23");
            order.setCcCVV("123");
            //placedAt
            //tacos
            order.addTaco(taco1);
            order.addTaco(taco2);
        }


        TacoOrder savedOrder = jdbcRepository.save(order);
        assertThat(savedOrder.getId()).isNotNull();

        Long savedOrderId = savedOrder.getId();
        TacoOrder fetchedOrder = jdbcRepository.findById(savedOrderId).get();
        assertThat(fetchedOrder).isNotNull();

        assertThat(fetchedOrder.getId()).isEqualTo(savedOrderId);
        assertThat(fetchedOrder.getDeliveryName()).isEqualTo("Test McTest");
        assertThat(fetchedOrder.getDeliveryStreet()).isEqualTo("1234 Test Lane");
        assertThat(fetchedOrder.getDeliveryCity()).isEqualTo("Testville");
        assertThat(fetchedOrder.getDeliveryState()).isEqualTo("CO");
        assertThat(fetchedOrder.getDeliveryZip()).isEqualTo("80123");
        assertThat(fetchedOrder.getCcNumber()).isEqualTo("4111111111111111");
        assertThat(fetchedOrder.getCcExpiration()).isEqualTo("10/23");
        assertThat(fetchedOrder.getCcCVV()).isEqualTo("123");
        assertThat(fetchedOrder.getPlacedAt()).isEqualTo(savedOrder.getPlacedAt());

        List<Taco> tacos = fetchedOrder.getTacos();
        assertThat(tacos.size()).isEqualTo(2);
        assertThat(tacos).containsExactlyInAnyOrder(taco1, taco2);
    }

    @Test
    void findById() {
    }
}