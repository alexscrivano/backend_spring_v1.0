package com.example.repositories;

import com.example.entities.Book;
import com.example.entities.BookLoan;
import com.example.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepo extends JpaRepository<BookLoan,Long> {
    BookLoan findByNumLoan(long num_loan);

    List<BookLoan> findByBooksContains(Book book);
    List<BookLoan> findByUser(User user);
}
