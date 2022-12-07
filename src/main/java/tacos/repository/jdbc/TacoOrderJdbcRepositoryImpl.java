package tacos.repository.jdbc;

import net.bytebuddy.jar.asm.Type;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.transaction.annotation.Transactional;
import tacos.model.IngredientRef;
import tacos.model.Taco;
import tacos.model.TacoOrder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;


public class TacoOrderJdbcRepositoryImpl implements TacoOrderJdbcRepository {

    private final JdbcOperations jdbcOperations;

    public TacoOrderJdbcRepositoryImpl(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }


    @Override
    @Transactional
    public TacoOrder save(TacoOrder tacoOrder) {

        /*
            Another thing that the save() method will need to do is determine what ID is
            assigned to the order once it has been saved. Per the schema, the id property on the
            Taco_Order table is an identity, meaning that the database will determine the value
            automatically. But if the database determines the value for you, then you will need to
            know what that value is so that it can be returned in the TacoOrder object returned
            from the save() method. Fortunately, Spring offers a helpful GeneratedKeyHolder
            type that can help with that. But it involves working with a prepared statement, as
            shown in the following implementation of the save() method:
         */
        PreparedStatementCreatorFactory pscf = null;
        {
            /*
                First, you create a PreparedStatement-
                CreatorFactory that describes the insert query along with the types of the query’s
                input fields.
             */
            String sqlStr = "insert into Taco_Order "

                    + "(delivery_name, delivery_street, delivery_city, "

                    + "delivery_state, delivery_zip, cc_number, "

                    + "cc_expiration, cc_cvv, placed_at) "

                    + "values (?,?,?, ?,?,?, ?,?,?)";

            pscf = new PreparedStatementCreatorFactory(sqlStr,

                    Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,

                    Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,

                    Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP);

            /*
                Because you’ll later need to fetch the saved order’s ID, you also will need
                to call setReturnGeneratedKeys(true).
             */
            pscf.setReturnGeneratedKeys(true);
        }

        PreparedStatementCreator psc = null;
        {
            /*
                After defining the PreparedStatementCreatorFactory, you use it to create a
                PreparedStatementCreator, passing in the values from the TacoOrder object that will
                be persisted. The last field given to the PreparedStatementCreator is the date that
                the order is created, which you’ll also need to set on the TacoOrder object itself so
                that the returned TacoOrder will have that information available.
             */
            tacoOrder.setPlacedAt(new Date());

            psc = pscf.newPreparedStatementCreator(

                    Arrays.asList(tacoOrder.getDeliveryName(), tacoOrder.getDeliveryStreet(), tacoOrder.getDeliveryCity(),

                            tacoOrder.getDeliveryState(), tacoOrder.getDeliveryZip(), tacoOrder.getCcNumber(),

                            tacoOrder.getCcExpiration(), tacoOrder.getCcCVV(), tacoOrder.getPlacedAt()));
        }

        long tacoOrderId = -1;
        {
            /*
                Now that you have a PreparedStatementCreator in hand, you’re ready to actually
                save the order data by calling the update() method on JdbcTemplate, passing in the
                PreparedStatementCreator and a GeneratedKeyHolder. After the order data has
                been saved, the GeneratedKeyHolder will contain the value of the id field as assigned
                by the database and should be copied into the TacoOrder object’s id property.
             */
            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcOperations.update(psc, keyHolder);

            tacoOrderId = keyHolder.getKey().longValue();
            tacoOrder.setId(tacoOrderId);
        }

        //saveTaco
        {
            /*
                At this point, the order has been saved, but you need to also save the Taco objects
                associated with the order. You can do that by calling saveTaco() for each Taco in
                the order.
             */
            saveTacos(tacoOrderId, tacoOrder.getTacos());
        }

        return tacoOrder;
    }

    private void saveTacos(Long tacoOrderId, List<Taco> tacoList) {

        int key = 0;
        for (Taco taco : tacoList) {
            saveTaco(tacoOrderId, key, taco);
        }
    }

    private long saveTaco(Long tacoOrderId, int tacoOrderKey, Taco taco) {

        PreparedStatementCreatorFactory pscf = null;
        {
            String sqlStr = "insert into Taco "

                    + "(name, created_at, taco_order, taco_order_key) "

                    + "values (?, ?, ?, ?)";

            pscf = new PreparedStatementCreatorFactory(sqlStr,

                    Types.VARCHAR, Types.TIMESTAMP, Type.LONG, Type.LONG);

            pscf.setReturnGeneratedKeys(true);
        }

        PreparedStatementCreator psc = null;
        {
            taco.setCreatedAt(new Date());

            psc = pscf.newPreparedStatementCreator(Arrays.asList(taco.getName(), taco.getCreatedAt(), tacoOrderId, tacoOrderKey));
        }

        long tacoId = -1;
        {
            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcOperations.update(psc, keyHolder);

            tacoId = keyHolder.getKey().longValue();
            taco.setId(tacoId);
        }

        //saveIngredientRefs
        {
            /*
                In the end, it makes a call to saveIngredientRefs() to create a
                row in the Ingredient_Ref table to link the Taco row to an Ingredient row
             */
            saveIngredientRefs(tacoId, taco.getIngredientsRefs());
        }

        return tacoId;
    }

    private void saveIngredientRefs(long tacoId, List<IngredientRef> ingredientRefList) {

        /*
            Thankfully, the saveIngredientRefs() method is much simpler. It cycles through a
            list of Ingredient objects, saving each into the Ingredient_Ref table. It also has a local
            key variable that is used as an index to ensure that the ordering of the ingredients
            stays intact.
         */
        int key = 0;
        for (IngredientRef ingredientRef : ingredientRefList) {

            String sqlStr = "insert into Ingredient_Ref (ingredient, taco, taco_key) values (?, ?, ?)";

            jdbcOperations.update(sqlStr, ingredientRef.getIngredient(), tacoId, key++);
        }
    }


    @Override
    public Optional<TacoOrder> findById(Long id) {

        try {

            String sqlStr = "select id, delivery_name, delivery_street, delivery_city, "

                    + "delivery_state, delivery_zip, cc_number, cc_expiration, "

                    + "cc_cvv, placed_at from Taco_Order where id=?";

            TacoOrder tacoOrder = jdbcOperations.queryForObject(sqlStr, this::mapRowToTacoOrder, id);

            return Optional.of(tacoOrder);

        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        }
    }

    private TacoOrder mapRowToTacoOrder(ResultSet row, int rowNum) throws SQLException {

        TacoOrder tacoOrder = new TacoOrder();
        {
            long tacoOrderId = row.getLong("id");
            tacoOrder.setId(tacoOrderId);
            tacoOrder.setDeliveryName(row.getString("delivery_name"));
            tacoOrder.setDeliveryStreet(row.getString("delivery_street"));
            tacoOrder.setDeliveryCity(row.getString("delivery_city"));
            tacoOrder.setDeliveryState(row.getString("delivery_state"));
            tacoOrder.setDeliveryZip(row.getString("delivery_zip"));
            tacoOrder.setCcNumber(row.getString("cc_number"));
            tacoOrder.setCcExpiration(row.getString("cc_expiration"));
            tacoOrder.setCcCVV(row.getString("cc_cvv"));
            tacoOrder.setPlacedAt(new Date(row.getTimestamp("placed_at").getTime()));
            tacoOrder.setTacos(findTacosByOrderId(tacoOrderId));
        }
        return tacoOrder;
    }

    private List<Taco> findTacosByOrderId(long tacoOrderId) {

        String sqlStr = "select id, name, created_at from Taco where taco_order=? order by taco_order_key";

        return jdbcOperations.query(sqlStr, this::mapRowToTaco, tacoOrderId);
    }

    private Taco mapRowToTaco(ResultSet row, int rowNum) throws SQLException {

        Taco taco = new Taco();
        {
            long tacoId = row.getLong("id");
            taco.setId(tacoId);
            taco.setName(row.getString("name"));
            taco.setCreatedAt(new Date(row.getTimestamp("created_at").getTime()));
            taco.setIngredientsRefs(findIngredientsByTacoId(tacoId));
        }
        return taco;
    }

    private List<IngredientRef> findIngredientsByTacoId(long tacoId) {

        String sqlStr = "select ingredient from Ingredient_Ref where taco = ? order by taco_key";

        return jdbcOperations.query(sqlStr, (row, rowNum) -> {
            return new IngredientRef(row.getString("ingredient"));
        }, tacoId);
    }
}
