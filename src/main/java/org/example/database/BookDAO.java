package org.example.database;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.example.models.Book;
import org.example.models.BorrowRecord;
import org.example.models.User;
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
        String sql = "SELECT b.*, u.name AS user_name " +
                "FROM books b " +
                "LEFT JOIN users u ON b.user_id = u.id";

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
                book.setBorrowedByUserName(rs.getString("user_name"));
                book.setUserId(rs.getInt("user_id"));
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
    public void updateBookAvailability(int id, boolean available, int userId) {
        String sql = "UPDATE books SET isAvailable = ?, borrowedDate = ?, user_id = ? WHERE id = ?";
        String dateStr = available ? null : LocalDate.now().toString();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBoolean(1, available);
            pstmt.setString(2, dateStr);

            if(available){
                pstmt.setNull(3, Types.INTEGER);
            }else{
                pstmt.setInt(3,userId);
            }
            pstmt.setInt(4,id);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                String historySql = "INSERT INTO borrow_history (book_id, user_id, action_type, action_date) VALUES (?, ?, ?, ?)";
                try(PreparedStatement hPstmt = conn.prepareStatement(historySql)){
                    hPstmt.setInt(1,id);
                    hPstmt.setInt(2,userId);

                    hPstmt.setString(3, available ?"RETURN" : "BORROW");

                    hPstmt.setString(4, LocalDate.now().toString());
                    hPstmt.executeUpdate();
                }
                //--------------------------------------

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
    public List<BorrowRecord> getBorrowHistory() {
        List<BorrowRecord> history = new ArrayList<>();

        String sql = "SELECT h.id, b.title, u.name, h.action_type, h.action_date " +
                "FROM borrow_history h " +
                "JOIN books b ON h.book_id = b.id " +
                "JOIN users u ON h.user_id = u.id " +
                "ORDER BY h.id DESC";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                history.add(new BorrowRecord(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("name"),
                        rs.getString("action_type"),
                        rs.getString("action_date")
                ));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error reading history: " + e.getMessage());
        }
        return history;
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
