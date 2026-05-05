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

        System.out.println("=== БИБЛИОТЕЧНА СИСТЕМА УПРАВЛЕНИЕ ===");

        while (running) {
            System.out.println("\nИзберете опция:");
            System.out.println("1. Виж всички налични книги");
            System.out.println("2. Добави нова книга");
            System.out.println("3. Изтриване");
            System.out.println("4. Търсене");
            System.out.println("5. Редактор");
            System.out.println("6. Изход");
            System.out.print("Вашият избор: ");

            if (!scanner.hasNextInt()) {
                String invalidInput = scanner.next(); // Взимаме грешния вход (напр. "ч")
                System.out.println("⚠️ Грешка: '" + invalidInput + "' не е валидно число! Моля, изберете 1, 2 или 3.");
                continue; // Връщаме се в началото на цикъла
            }

            int choice = scanner.nextInt();
            scanner.nextLine(); // Изчистваме буфера

            switch (choice) {
                case 1:
                    // ТОВА ОСТАВА: Логиката за извличане на списъка
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
                    // ТОВА СЕ ПРОМЕНЯ: Вече не е твърдо написано, а чете от теб
                    System.out.print("Заглавие: ");
                    String title = scanner.nextLine();
                    System.out.print("Автор: ");
                    String author = scanner.nextLine();

                    bookDAO.addBook(new Book(0, title, author, "ISBN-123", true));
                    break;

                case 3:

                    System.out.print("Въведете ID на книгата за изтриване: ");
                    if (scanner.hasNextInt()) {
                        int id = scanner.nextInt();
                        scanner.nextLine();
                        bookDAO.deleteBook(id);
                    } else {
                        System.out.println("⚠️ Невалидно ID!");
                        scanner.next();
                    }
                    break;
                case 4:
                    System.out.println("Въведете заглавие или част от него за търсене: ");
                    String searchTerm = scanner.nextLine();
                    List<Book> foundBook = bookDAO.searchBookByTitle(searchTerm);
                    if(searchTerm.isEmpty()) {
                        System.out.println("⚠️ Моля, въведете дума за търсене.");
                        break;
                    }
                    System.out.println("\n--- РЕЗУЛТАТИ ОТ ТЪРСЕНЕТО ---\n");
                    if(foundBook.isEmpty()){
                        System.out.println("Няма намерени книги, съвпадащи с: " + searchTerm);
                    }else{
                        for(Book b : foundBook){
                            System.out.println("ID: " + b.getId() + " | " + b.getTitle() + " (" +
                                    b.getAuthor() + ") ");
                        }
                    }
                    break;
                case 5:
                    System.out.println("Въведете ID на книгата за редакция: ");
                    if(scanner.hasNextInt()){
                        int updateId = scanner.nextInt();
                        scanner.nextLine();

                        System.out.println("Ново заглавие: ");
                        String newTitle = scanner.nextLine();
                        System.out.println("Нов автор: ");
                        String newAuthor = scanner.nextLine();

                        bookDAO.updateBook(updateId, newTitle, newAuthor);
                    }else {
                        System.out.println("⚠ Невалидно ID!");
                        scanner.next();
                    }break;
                case 6:
                    running = false;
                    System.out.println("👋 Довиждане!");
                    break;


                default:
                    System.out.println("⚠️ Няма такава опция.");
            }
        }
        scanner.close();
    }
}