package com.example.productapi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/tienda";
    private static final String USER = "root"; // cambia si tu usuario es diferente
    private static final String PASSWORD = "123456"; // pon tu contraseña de MySQL

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
