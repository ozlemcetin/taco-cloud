package tacos.repository;

import net.bytebuddy.jar.asm.Type;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tacos.model.IngredientRef;
import tacos.model.Taco;
import tacos.model.TacoOrder;

import java.sql.Types;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Repository
public class TacoOrderRepositoryJdbcImpl implements TacoOrderRepository {

    private final JdbcOperations jdbcOperations;

    public TacoOrderRepositoryJdbcImpl(JdbcOperations jdbcOperations) {
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
            List<Taco> tacos = tacoOrder.getTacos();
            int i = 0;
            for (Taco taco : tacos) {
                saveTaco(tacoOrderId, i++, taco);
            }
        }

        return tacoOrder;
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
            saveIngredientRefs(tacoId, taco.getIngredients());
        }

        return tacoId;
    }

    private void saveIngredientRefs(long tacoId, List<IngredientRef> ingredientRefs) {

        /*
            Thankfully, the saveIngredientRefs() method is much simpler. It cycles through a
            list of Ingredient objects, saving each into the Ingredient_Ref table. It also has a local
            key variable that is used as an index to ensure that the ordering of the ingredients
            stays intact.
         */
        int key = 0;
        for (IngredientRef ingredientRef : ingredientRefs) {

            String sqlStr = "insert into Ingredient_Ref (ingredient, taco, taco_key) " + "values (?, ?, ?)";

            jdbcOperations.update(sqlStr, ingredientRef.getIngredient(), tacoId, key++);
        }
    }
}
