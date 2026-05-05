package org.example;

import org.example.database.DatabaseConfig;


public class Main {
    public static void main(String[] args) {
        // Извикваме инициализацията още при стартиране
        DatabaseConfig.initializeDatabase();

        System.out.println("Програмата стартира успешно!");
    }
}