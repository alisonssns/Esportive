package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conector {
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {  
            
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/esportive", "root", "1234");
        }
        return connection;
    }
}
