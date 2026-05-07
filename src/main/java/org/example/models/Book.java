package org.example.models;

public class Book {
    private int id;
    private String title;
    private String author;
    private String isbn;
    private boolean isAvailable;
    private String borrowedDate;

    public Book(int id, String title, String author, String isbn, boolean isAvailable,String borrowedDate) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.isAvailable = isAvailable;
        this.borrowedDate = borrowedDate;
    }

    public Book(int id, String title, String author, String isbn, boolean isAvailable) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.isAvailable = isAvailable;
        this.borrowedDate = null;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public String getBorrowedDate() {
        return borrowedDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public void setBorrowedDate(String borrowedDate) {
        this.borrowedDate = borrowedDate;
    }
}
