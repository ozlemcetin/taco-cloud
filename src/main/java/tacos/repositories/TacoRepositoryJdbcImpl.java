package tacos.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import tacos.entities.Ingredient;
import tacos.entities.Taco;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;

@Repository
public class TacoRepositoryJdbcImpl implements TacoRepositoryInt {

    private final JdbcTemplate jdbc;

    @Autowired
    public TacoRepositoryJdbcImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Taco save(Taco taco) {

        long tacoId = saveTacoInfo(taco);
        taco.setId(tacoId);

        for (Ingredient ingredient : taco.getIngredients()) {
            saveIngredientToTaco(tacoId, ingredient.getId());
        }

        return taco;
    }

    private long saveTacoInfo(Taco taco) {

        taco.setCreatedAt(new Date());

        String sql = "insert into Taco (name, createdAt ) values (?, ?)";
        PreparedStatementCreatorFactory factory =
                new PreparedStatementCreatorFactory(sql, Types.VARCHAR, Types.TIMESTAMP);

        /*
        You have to instruct PreparedStatementCreatorFactory instance to return the generated keys:
        By default, returnGeneratedKeys = false so change it to true
        */
        factory.setReturnGeneratedKeys(true);

        PreparedStatementCreator creator = factory
                .newPreparedStatementCreator(
                        Arrays.asList(taco.getName(), new Timestamp(taco.getCreatedAt().getTime())));


        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(creator, keyHolder);

        Number key = keyHolder.getKey();
        return key.longValue();
    }

    private void saveIngredientToTaco(long tacoId, String ingredientId) {

        String sql = "insert into Taco_Ingredients (taco, ingredient ) values (?, ?)";
        jdbc.update(sql, tacoId, ingredientId);

    }


}
