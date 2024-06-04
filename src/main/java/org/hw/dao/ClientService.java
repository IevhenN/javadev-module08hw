package org.hw.dao;

import lombok.Data;
import org.hw.db.DataBase;
import org.hw.entity.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Data
public class ClientService {
    private static final String INSERT_INTO_CLIENT = "INSERT INTO client(name) VALUES(?) RETURNING id";
    private static final String SELECT_BY_ID = "SELECT name FROM client WHERE id = ?";
    private static final String UPDATE_BY_NAME = "UPDATE client SET name = ? WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM client WHERE id = ?";
    private static final String SELECT_ALL = "SELECT id, name FROM client";

    private Connection connection;

    public ClientService() {
        connection = DataBase.getInstance().getConnection();
    }

    public long create(String name) {
        long result = -1;
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTO_CLIENT)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getLong("id");
            }
        } catch (SQLException e) {
            return result;
        }

        return result;
    }

    public String getById(long id) {
        String result = "";
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getString("name");
            }
        } catch (SQLException e) {
            return result;
        }
        return result;
    }

    public void setName(long id, String name) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BY_NAME)) {
            preparedStatement.setString(1, name);
            preparedStatement.setLong(2, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteById(long id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_ID);
        preparedStatement.setLong(1, id);
        preparedStatement.execute();
        preparedStatement.close();
    }

    public List<Client> listAll() {
        List<Client> result = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result.add(new Client(resultSet.getLong("id"), resultSet.getString("name")));
            }
        } catch (SQLException e) {
            return result;
        }
        return result;
    }
}
