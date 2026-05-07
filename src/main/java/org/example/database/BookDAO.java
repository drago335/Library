package org.example.database;

import java.sql.Statement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.example.models.Book;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;


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
            System.out.println("✅ Book '" + book.getTitle() + "' has been saved successfully!");

        } catch (SQLException e) {
            System.out.println("❌ Error saving / Save error: " + e.getMessage());
        }
    }
    public void deleteBook(int id) {
        String sql = "DELETE FROM books WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("✅ Book with ID " + id + " was deleted successfully!");
            } else {
                System.out.println("⚠️ No found book with ID " + id + ".");
            }

        } catch (SQLException e) {
            System.out.println("❌ Error deleting from the database: " + e.getMessage());
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
                        rs.getBoolean("isAvailable"),
                        rs.getString("borrowedDate")

                );
                books.add(book);
            }
        } catch (SQLException e) {
            System.out.println("❌ Reading error / Error while reading: " + e.getMessage());
        }
        return books;
    }
    public List<Book> searchBookByTitle(String searchTerm){
        List<Book> allBooks = getAllBooks();
        List<Book> filteredBooks = new ArrayList<>();

        String searchLower = searchTerm.toLowerCase().trim();

        for (Book book : allBooks) {

            if (book.getTitle().toLowerCase().contains(searchLower)) {
                filteredBooks.add(book);
            }
        }

        return filteredBooks;
    }

    public void updateBook(int id,String newTitle,String newAuthor) {
        String sql  = "UPDATE books SET title = ?, author = ? WHERE id = ?";

        try(Connection conn = DatabaseConfig.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1,newTitle);
            pstmt.setString(2,newAuthor);
            pstmt.setInt(3,id);

            int affectedRows = pstmt.executeUpdate();
            if(affectedRows > 0){
                System.out.printf("✅ The book with ID %d was updated successfully!%n",id);
            } else {
                System.out.println("⚠ No book was found with ID " + id + ".");
            }
        }catch (SQLException e){
            System.out.println("❌ Edit error / Error during editing:" + e.getMessage());
        }
    }
    public void updateBookAvailability(int id, boolean available) {
        //Logic: If get book(available = false) , get today date
        //If return book(available = true ), date = null
        String dateStr = available ? null : LocalDate.now().toString();

        String sql = "UPDATE books SET isAvailable = ?, borrowedDate = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBoolean(1, available);
            pstmt.setString(2, dateStr);
            pstmt.setInt(3,id);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                String action = available ? "returned" : "borrowed";
                if(!available){
                    System.out.println("✅ Book with ID " + id + " was successfully " + action + " on " + dateStr + "!");
                }else {
                    System.out.println("✅ Book with ID " + id + " was successfully " + action + "!");
                }
            } else {
                System.out.println("⚠️ No book found with ID " + id + ".");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error updating status: " + e.getMessage());
        }
    }
    public Book getBookById(int id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn"),
                        rs.getBoolean("isAvailable"),
                        rs.getString("borrowedDate")
                );
            }
        } catch (SQLException e) {
            System.out.println("❌ Error finding book: " + e.getMessage());
        }
        return null; // If book not exist
    }
}
