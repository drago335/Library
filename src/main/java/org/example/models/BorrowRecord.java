package org.example.models;

public class BorrowRecord {
    private int id;
    private String bookTitle;
    private String userName;
    private String actionType; // "BORROW" or "RETURN"
    private String date;

    public BorrowRecord(int id, String bookTitle, String userName, String actionType, String date) {
        this.id = id;
        this.bookTitle = bookTitle;
        this.userName = userName;
        this.actionType = actionType;
        this.date = date;
    }

    @Override
    public String toString() {
        return String.format("Log ID: %d | Book: %-15s | User: %-10s | Action: -%-7s | Data: %s",
                id,bookTitle,userName,actionType,date);
    }
}
