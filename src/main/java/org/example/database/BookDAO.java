package org.example.database;

import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.example.models.Book;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class BookDAO {

    public void addBook(Book book) {

        String sql = "INSERT INTO books (title, author, isbn, isAvailable) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {


            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getIsbn());
            pstmt.setBoolean(4, book.isAvailable());

            // Изпълняваме записа
            pstmt.executeUpdate();
            System.out.println("✅ Книгата '" + book.getTitle() + "' е записана успешно!");

        } catch (SQLException e) {
            System.out.println("❌ Грешка при запис: " + e.getMessage());
        }
    }
    public void deleteBook(int id) {
        String sql = "DELETE FROM books WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("✅ Книгата с ID " + id + " беше изтрита успешно!");
            } else {
                System.out.println("⚠️ Не беше намерена книга с ID " + id + ".");
            }

        } catch (SQLException e) {
            System.out.println("❌ Грешка при изтриване от базата: " + e.getMessage());
        }
    }
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Book book = new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn"),
                        rs.getBoolean("isAvailable")
                );
                books.add(book);
            }
        } catch (SQLException e) {
            System.out.println("❌ Грешка при четене: " + e.getMessage());
        }
        return books;
    }

}
