package tacos.repository.jdbc;

import tacos.model.Ingredient;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IngredientRepositoryJdbcRawImpl implements IngredientJdbcRepository {

    private final javax.sql.DataSource dataSource;

    public IngredientRepositoryJdbcRawImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Ingredient> findAll() {

        List<Ingredient> list = new ArrayList<>();
        {
            //creates a connection
            try (Connection connection = dataSource.getConnection()) {

                String sqlStr = "select id, name, type from Ingredient";

                //creates a statement,
                try (PreparedStatement statement = connection.prepareStatement(sqlStr)) {

                    //creates a result set
                    try (ResultSet resultSet = statement.executeQuery()) {

                        while (resultSet.next()) {
                            Ingredient ingredient = mapRowToIngredient(resultSet);
                            list.add(ingredient);
                        }
                    }//ResultSet
                }//PreparedStatement

            } catch (SQLException e) {
                // What should be done here ???
            }
        }
        return list;
    }


    @Override
    public Optional<Ingredient> findById(String id) {

        /*
            SQLException is a checked exception, which requires handling in a catch block.
            But the most common problems, such as failure to create a connection to the database
            or a mistyped query, can’t possibly be addressed in a catch block and are likely to
            be rethrown for handling upstream.
         */
        Ingredient ingredient = null;
        {
            //creates a connection
            try (Connection connection = dataSource.getConnection()) {

                String sqlStr = "select id, name, type from Ingredient where id=?";

                //creates a statement,
                try (PreparedStatement statement = connection.prepareStatement(sqlStr)) {

                    statement.setString(1, id);

                    //creates a result set
                    try (ResultSet resultSet = statement.executeQuery()) {

                        if (resultSet.next()) {
                            ingredient = mapRowToIngredient(resultSet);
                        }
                    }//ResultSet
                }//PreparedStatement

            } catch (SQLException e) {
                // What should be done here ???
            }
        }
        return Optional.of(ingredient);
    }

    private Ingredient mapRowToIngredient(ResultSet row) throws SQLException {

        String idStr = row.getString("id");
        String nameStr = row.getString("name");
        Ingredient.Type type = Ingredient.Type.valueOf(row.getString("type"));

        return new Ingredient(idStr, nameStr, type);
    }

    @Override
    public Ingredient save(Ingredient ingredient) {
        return null;
    }

    @Override
    public List<Ingredient> filterByType(Ingredient.Type type) {
        return null;
    }
}
