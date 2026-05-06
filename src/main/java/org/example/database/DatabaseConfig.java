package org.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConfig {


    private static final String URL = "jdbc:sqlite:library.db";


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }


    public static void initializeDatabase() {

        String sql = "CREATE TABLE IF NOT EXISTS books (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT NOT NULL," +
                "author TEXT NOT NULL," +
                "isbn TEXT," +
                "isAvailable BOOLEAN DEFAULT 1" +
                ");";


        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("✅ The database and table are ready!");

        } catch (SQLException e) {
            System.out.println("❌ Error creating the database: " + e.getMessage());
        }
    }
}
