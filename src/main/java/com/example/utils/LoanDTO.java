package com.example.utils;

import com.example.entities.Book;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Component
public class LoanDTO {
    long num_loan;
    Date date;
    Date returnDate;
    List<Book> books;
}
