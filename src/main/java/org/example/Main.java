package org.example;

import org.example.models.Book;
import org.example.database.BookDAO;
import org.example.database.DatabaseConfig;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DatabaseConfig.initializeDatabase();
        BookDAO bookDAO = new BookDAO();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("=== LIBRARY SYSTEM ===");

        while (running) {
            System.out.println("\nChose option:");
            System.out.println("1. View all available books");
            System.out.println("2. Add a new book");
            System.out.println("3. Delete");
            System.out.println("4. Search");
            System.out.println("5. Editor");
            System.out.println("6. Exit");
            System.out.print("Your choice: ");

            if (!scanner.hasNextInt()) {
                String invalidInput = scanner.next(); // Handling invalid input (e.g., "ch")
                System.out.println("⚠️ Wrong: '" + invalidInput + "' is not a valid number! Please choose 1, 2, or 3");
                continue; // Returning to the start of the loop
            }

            int choice = scanner.nextInt();
            scanner.nextLine(); // Clearing the buffer

            switch (choice) {
                case 1:
                    // THIS REMAINS: The logic for retrieving the list
                    List<Book> books = bookDAO.getAllBooks();
                    System.out.println("\n--- НАЛИЧНИ КНИГИ ---");
                    if (books.isEmpty()) {
                        System.out.println("Няма намерени книги.");
                    } else {
                        for (Book b : books) {
                            System.out.println("ID: " + b.getId() + " | " + b.getTitle() + " - " + b.getAuthor());
                        }
                    }
                    break;

                case 2:
                    // THIS CHANGES: It is no longer hardcoded; it reads from your input
                    System.out.print("Title: ");
                    String title = scanner.nextLine();
                    System.out.print("Author: ");
                    String author = scanner.nextLine();

                    bookDAO.addBook(new Book(0, title, author, "ISBN-123", true));
                    break;

                case 3:

                    System.out.print("Enter the book ID to delete: ");
                    if (scanner.hasNextInt()) {
                        int id = scanner.nextInt();
                        scanner.nextLine();
                        bookDAO.deleteBook(id);
                    } else {
                        System.out.println("⚠️ Invalid ID!");
                        scanner.next();
                    }
                    break;
                case 4:
                    System.out.println("Enter a title or part of it to search: ");
                    String searchTerm = scanner.nextLine();
                    List<Book> foundBook = bookDAO.searchBookByTitle(searchTerm);
                    if(searchTerm.isEmpty()) {
                        System.out.println("⚠️ Please enter a search term.");
                        break;
                    }
                    System.out.println("\n--- SEARCH RESULTS ---\n");
                    if(foundBook.isEmpty()){
                        System.out.println("No books found matching: " + searchTerm);
                    }else{
                        for(Book b : foundBook){
                            System.out.println("ID: " + b.getId() + " | " + b.getTitle() + " (" +
                                    b.getAuthor() + ") ");
                        }
                    }
                    break;
                case 5:
                    System.out.println("Enter the book ID to edit: ");
                    if(scanner.hasNextInt()){
                        int updateId = scanner.nextInt();
                        scanner.nextLine();

                        System.out.println("New Title: ");
                        String newTitle = scanner.nextLine();
                        System.out.println("New Author: ");
                        String newAuthor = scanner.nextLine();

                        bookDAO.updateBook(updateId, newTitle, newAuthor);
                    }else {
                        System.out.println("⚠ Invalid ID!");
                        scanner.next();
                    }break;
                case 6:
                    running = false;
                    System.out.println("👋 Goodbye!");
                    break;


                default:
                    System.out.println("⚠️ Invalid option / No such option.");
            }
        }
        scanner.close();
    }
}