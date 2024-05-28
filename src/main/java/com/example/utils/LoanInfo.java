package com.example.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class LoanInfo {
    String userEmail;
    String book_isbn;
    String[] isbn_list;

    public LoanInfo(){}

    public LoanInfo(String userEmail, long id, String book_isbn, String[] isbn_list) {
        this.userEmail = userEmail;
        this.book_isbn = book_isbn;
        this.isbn_list = isbn_list;
    }
    public LoanInfo(String userEmail, String book_isbn) {
        this.userEmail = userEmail;
        this.book_isbn = book_isbn;
    }
}
