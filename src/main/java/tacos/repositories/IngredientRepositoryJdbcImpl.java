package tacos.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import tacos.entities.Ingredient;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class IngredientRepositoryJdbcImpl implements IngredientRepositoryInt {

    private final JdbcTemplate jdbc;

    @Autowired
    public IngredientRepositoryJdbcImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /*
    consider the methods that use Jdbc-Template.
     */

    @Override
    public Iterable<Ingredient> findAll() {

        String sql = "select id, name, type from Ingredient";
        return jdbc.query(sql, this::mapRowToIngredient);
    }


    @Override
    public Ingredient findById(String id) {

        String sql = "select id, name, type from Ingredient where id=?";
        return jdbc.queryForObject(sql, this::mapRowToIngredient, id);
    }

    /*
    Java 8’s method references and lambdas are convenient when working with JdbcTemplate as
    an alternative to an explicit RowMapper implementation.
     */
    private Ingredient mapRowToIngredient(ResultSet resultSet, int rowNumber) throws SQLException {

        return new Ingredient(
                resultSet.getString("id"),
                resultSet.getString("name"),
                Ingredient.Type.valueOf(resultSet.getString("type")));
    }


    /*
    preJava8RowMapper - if for some reason you
    want or need an explicit RowMapper, then the following implementation of findAll()
    shows how to do that:
     */
    public Ingredient findOne_JdbcTemplate(String id) {

        String sql = "select id, name, type from Ingredient where id=?";
        return jdbc.queryForObject(sql, new RowMapper<Ingredient>() {

            @Override
            public Ingredient mapRow(ResultSet resultSet, int i) throws SQLException {
                return new Ingredient(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        Ingredient.Type.valueOf(resultSet.getString("type")));
            }
        }, id);
    }

    @Override
    public Ingredient save(Ingredient ingredient) {

        /*
        JdbcTemplate’s update() method can be used for any query that writes or updates
        data in the database
         */
        String sql = "insert into Ingredient (id, name, type) values (?, ?, ?)";
        jdbc.update(sql, ingredient.getId(),
                ingredient.getName(),
                ingredient.getType().toString());

        return ingredient;
    }
}
