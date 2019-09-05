package com.revolut.moneytransfer.dao;

import com.revolut.moneytransfer.model.Account;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {

    private Connection connection;

    JdbcTemplate(){
        connection = ConnectionFactory.getConnection();
    }

    public <T> List<T> executeForList (String query, RowMapper<T> mapper){
        List<T> result = new ArrayList<>();

        try {
            if(connection.isClosed()) {
                connection = ConnectionFactory.getConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                result.add(mapper.mapRow(resultSet, resultSet.getRow()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public <T> T executeForObject (String query, RowMapper<T> mapper, Object ... parameters){
        List<T> result = new ArrayList<>();

        try {
            if(connection.isClosed()) {
                connection = ConnectionFactory.getConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (PreparedStatement statement = connection.prepareStatement(query)){

            for (int i = 0; i < parameters.length; i++) {
                statement.setObject(i + 1, parameters[i]);
            }
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                result.add(mapper.mapRow(resultSet, resultSet.getRow()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return result.isEmpty() ? null : result.get(0);
    }

    public void execute(String query, Object... parameters) {
        try {
            if(connection.isClosed()) {
                connection = ConnectionFactory.getConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (PreparedStatement statement = connection.prepareStatement(query);){
            for (int i = 0; i < parameters.length; i++) {
                statement.setObject(i + 1, parameters[i]);
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void transfer(Account accountFrom, Account accountTo) {
        Connection connection = null;
        try {
            connection = ConnectionFactory.getConnection();
            connection.setAutoCommit(false);

            execute("UPDATE accounts SET amount = ? WHERE id = ?", accountFrom.getAmount(), accountFrom.getId());
            execute("UPDATE accounts SET amount = ? WHERE id = ?", accountTo.getAmount(), accountTo.getId());

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
