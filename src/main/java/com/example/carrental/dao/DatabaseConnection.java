package com.example.carrental.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection conexaoMySQL;
    private Connection conexao;

    private DatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Conectado com sucesso!");
            conexao = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3307/car_rental?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
                    "teste",
                    "senhaTeste"
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static DatabaseConnection getInstance() {
        if (conexaoMySQL == null) {
            conexaoMySQL = new DatabaseConnection();
        }

        return conexaoMySQL;
    }

    public Connection getConexao() {
        return conexao;
    }
}