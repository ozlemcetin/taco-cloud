package tacos.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import tacos.model.Ingredient;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class IngredientJdbcRepositoryImpl implements IngredientJdbcRepository {

    /*
        Although the interface captures the essence of what you need an ingredient repository
        to do, you’ll still need to write an implementation of IngredientRepository that
        uses JdbcTemplate to query the database
     */

    /*
        As you can see, JdbcIngredientRepository is annotated with @Repository. This
        annotation is one of a handful of stereotype annotations that Spring defines, including
        @Controller and @Component. By annotating JdbcIngredientRepository with
        @Repository, you declare that it should be automatically discovered by Spring component
        scanning and instantiated as a bean in the Spring application context.
     */

    /*
        When Spring creates the JdbcIngredientRepository bean, it injects it with Jdbc-
        Template. That’s because when there’s only one constructor, Spring implicitly applies
        autowiring of dependencies through that constructor’s parameters. If there is more
        than one constructor, or if you just want autowiring to be explicitly stated, then you
        can annotate the constructor with @Autowired as follows:
     */

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public IngredientJdbcRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /*
               there aren’t any statements or connections being created. And, after the
               method is finished, there isn’t any cleanup of those objects. Finally, there isn’t any
               handling of exceptions that can’t properly be handled in a catch block. What’s left is
               code that’s focused solely on performing a query (the call to JdbcTemplate’s query()
               method) and mapping the results to an Ingredient object (handled by the mapRow-
               ToIngredient() method).
    */

    @Override
    public Iterable<Ingredient> findAll() {

        /*
            The query() method accepts the SQL for the query as well as an implementation
            of Spring’s RowMapper for the purpose of mapping each row in the result set to
            an object. query() also accepts as its final argument(s) a list of any parameters
            required in the query. But, in this case, there aren’t any required parameters
         */
        String sqlStr = "select id, name, type from Ingredient";

        return jdbcTemplate.query(sqlStr, this::mapRowToIngredient);
    }


    @Override
    public Optional<Ingredient> findById(String id) {

        /*
            In contrast, the findById() method will need to include a where clause in its
            query to compare the value of the id column with the value of the id parameter
            passed into the method. Therefore, the call to query() includes, as its final parameter,
            the id parameter. When the query is performed, the ? will be replaced with this value.

            new RowMapper<Ingredient>() {
                public Ingredient mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new Ingredient(rs.getString("id"), rs.getString("name"), Ingredient.Type.valueOf(rs.getString("type")));
                };
            },

         */
        String sqlStr = "select id, name, type from Ingredient where id=?";

        List<Ingredient> results = jdbcTemplate.query(sqlStr, this::mapRowToIngredient, id);

        return results.size() == 0 ? Optional.empty() : Optional.of(results.get(0));
    }

    private Ingredient mapRowToIngredient(ResultSet row, int rowNum) throws SQLException {

        String idStr = row.getString("id");
        String nameStr = row.getString("name");
        Ingredient.Type type = Ingredient.Type.valueOf(row.getString("type"));

        return new Ingredient(idStr, nameStr, type);
    }

    @Override
    public Ingredient save(Ingredient ingredient) {

        /*
            JdbcTemplate’s update() method can be used for any query that writes or updates
            data in the database. And, as shown in the following listing, it can be used to insert
            data into the database.
         */
        String sqlStr = "insert into Ingredient ( id, name, type) values (?, ?, ?)";

        //the update() method
        jdbcTemplate.update(sqlStr, ingredient.getId(), ingredient.getName(), ingredient.getType().toString());

        return ingredient;
    }

    @Override
    public List<Ingredient> filterByType(Ingredient.Type type) {

        String sqlStr = "select id, name, type from Ingredient where type=?";

        return jdbcTemplate.query(sqlStr, this::mapRowToIngredient, type.toString());
    }

}
