package tacos.repositories;

import tacos.entities.Ingredient;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IngredientRepositoryRawJdbcImpl implements IngredientRepositoryInt {

    private final DataSource dataSource;

    public IngredientRepositoryRawJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    /*
        how to perform a simple query in Java without JdbcTemplate.
    */

    @Override
    public Iterable<Ingredient> findAll() {

        List<Ingredient> ingredients = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {

            String sql = "select id, name, type from Ingredient";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {

                try (ResultSet resultSet = statement.executeQuery()) {

                    Ingredient ingredient = null;
                    while (resultSet.next()) {

                        ingredient = new Ingredient(
                                resultSet.getString("id"),
                                resultSet.getString("name"),
                                Ingredient.Type.valueOf(resultSet.getString("type")));

                        //add list
                        ingredients.add(ingredient);
                    }
                }
            }

        } catch (SQLException e) {
            // ??? What should be done here ???
        }

        return ingredients;
    }

    @Override
    public Ingredient findById(String id) {

        Ingredient ingredient = null;

        try (Connection connection = dataSource.getConnection()) {

            String sql = "select id, name, type from Ingredient where id=?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setString(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {

                    if (resultSet.next()) {

                        ingredient = new Ingredient(
                                resultSet.getString("id"),
                                resultSet.getString("name"),
                                Ingredient.Type.valueOf(resultSet.getString("type")));
                    }
                }
            }

        } catch (SQLException e) {
            // ??? What should be done here ???
        }

        return ingredient;
    }

    @Override
    public Ingredient save(Ingredient ingredient) {
        return null;
    }


}
