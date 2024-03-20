package com.example.asassignment;

public class Book {
    String name;
    public int cat_num;
    String author;
    String pub;
    String ISBN_10;
    String ISBN_13;
    String Review;

    public Book(String bookName, int catalogueNumber, String authorName, String publisher, String isbn10, String isbn13, String review) {
        cat_num = catalogueNumber;
        author = authorName;
        pub = publisher;
        ISBN_10 = isbn10;
        ISBN_13 = isbn13;
        name = bookName;
        Review = review;
    }
}
