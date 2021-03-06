package tacos.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import tacos.entities.Order;
import tacos.entities.Taco;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Repository
public class OrderRepositoryJdbcImpl implements OrderRepositoryInt {

    private final JdbcTemplate jdbc;

    @Autowired
    public OrderRepositoryJdbcImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Order save(Order order) {

        long orderId = saveOrderDetails(order);
        order.setId(orderId);

        for (Taco taco : order.getTacos()) {
            saveTacoToOrder(orderId, taco.getId());
        }

        return order;
    }

    private long saveOrderDetails(Order order) {

        order.setPlacedAt(new Date());

        /*
        configured to work with the Taco_Order table and to assume that the id property will be
        provided or generated by the database.
         */
        SimpleJdbcInsert orderInserter = new SimpleJdbcInsert(jdbc)
                .withTableName("Taco_Order")
                .usingGeneratedKeyColumns("id");

        /*
        the map keys correspond to the column names in the table the data is inserted into

        Order has several properties, and those properties all share the same name
        with the columns that they’re going into.

        Because of that, in saveOrderDetails(), I’ve decided to use Jackson’s ObjectMapper
        and its convertValue() method to convert an Order into a Map.
         */

        @SuppressWarnings("unchecked")
        Map<String, Object> values =
                new ObjectMapper().convertValue(order, Map.class);

        /*
        Once the Map is created, you’ll set the placedAt entry to the
        value of the Order object’s placedAt property.

        This is necessary because Object-Mapper would otherwise convert
        the Date property into a long, which is incompatible
        with the placedAt field in the Taco_Order table.
         */
        values.put("placedAt", order.getPlacedAt());

        long orderId =
                orderInserter
                        .executeAndReturnKey(values)
                        .longValue();

        return orderId;
    }

    private void saveTacoToOrder(long orderId, long tacoId) {

        /*
        Two ways to save data with JdbcTemplate include the following:
        Directly, using the update() method
        Using the SimpleJdbcInsert wrapper class
         */

        //String sql = "insert into Taco_Order_Tacos (tacoOrder, taco ) values (?, ?)";
        //jdbc.update(sql, orderId, tacoId);

        SimpleJdbcInsert orderTacoInserter = new SimpleJdbcInsert(jdbc)
                .withTableName("Taco_Order_Tacos");


        Map<String, Object> values = new HashMap<>();
        values.put("tacoOrder", orderId);
        values.put("taco", tacoId);

        orderTacoInserter.execute(values);
    }

}
