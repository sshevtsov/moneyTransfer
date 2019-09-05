package com.revolut.moneytransfer.dao;

import org.h2.jdbcx.JdbcConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionFactory {
    private static final String DB_Driver = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:mem:test";

    static {
        initTable();
    }

    public static Connection getConnection() {
        JdbcConnectionPool connectionPool = null;
        try {
                Class.forName(DB_Driver);
                connectionPool = JdbcConnectionPool.create(DB_URL, "sa", "");
                connectionPool.setMaxConnections(10);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
        }
        try {
            return connectionPool.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static void initTable() {
        try (Statement statement = getConnection().createStatement()){

            statement.execute("CREATE TABLE IF NOT EXISTS accounts" +
                                    "(id int auto_increment primary key, " +
                                    "amount int)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
