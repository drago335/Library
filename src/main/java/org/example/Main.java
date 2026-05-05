package org.example;

import org.example.models.Book;
import org.example.database.BookDAO;
import org.example.database.DatabaseConfig;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Извикваме инициализацията още при стартиране
        DatabaseConfig.initializeDatabase();

        // 2. Създаваме обект Book (нашето Java представяне)
        // Използваме 0 за ID, защото базата автоматично ще му даде номер
        Book newBook = new Book(0, "Под игото", "Иван Вазов", "978-954", true);

        // 3. Използваме BookDAO, за да пратим обекта към базата
        BookDAO bookDAO = new BookDAO();
        bookDAO.addBook(newBook);

        // 4. Извличаме всички книги, които вече са записани в library.db
        List<Book> library = bookDAO.getAllBooks();

        // 5. Принтираме резултата в конзолата
        System.out.println("\n--- ТЕКУЩ СПИСЪК С КНИГИ ---");
        if (library.isEmpty()) {
            System.out.println("Библиотеката е празна.");
        } else {
            for (Book b : library) {
                System.out.println("ID: " + b.getId() +
                        " | Заглавие: " + b.getTitle() +
                        " | Автор: " + b.getAuthor() +
                        " | Налична: " + (b.isAvailable() ? "Да" : "Не"));
            }
        }

        System.out.println("----------------------------");
        System.out.println("Програмата приключи успешно!");
    }
}