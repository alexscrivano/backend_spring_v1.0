package com.example.services;

import com.example.entities.Book;
import com.example.entities.BookLoan;
import com.example.entities.User;
import com.example.repositories.BookRepo;
import com.example.repositories.LoanRepo;
import com.example.repositories.ShelfRepo;
import com.example.repositories.UserRepo;
import jakarta.persistence.LockModeType;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServices {
    @Autowired
    UserRepo userRepository;
    @Autowired
    BookRepo bookRepository;
    @Autowired
    LoanRepo loanRepository;
    @Autowired
    ShelfRepo shelfRepository;

    //Loans
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Lock(LockModeType.OPTIMISTIC)
    public BookLoan makeAsingleLoan(User user, Book book) throws Exception {
        if(userRepository.existsByEmail(user.getEmail())) {
            if(book.getCopies() > 0){
                BookLoan bookLoan = new BookLoan();
                bookLoan.setUser(user);
                List<Book> books = List.of(book);
                return loanRepository.save(bookLoan);
            }else throw new Exception("Non ci sono copie disponibili");
        }else throw new Exception("Problema nella prenotazione del libro: " + book.getTitle());
    }
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Lock(LockModeType.OPTIMISTIC)
    public BookLoan makeALoan(User user, List<Book> books) throws Exception {
        if(userRepository.existsByEmail(user.getEmail())){
            for(Book book : books){
                if(!bookRepository.existsByISBN(book.getISBN())) throw new Exception("Libro " + book.getISBN() + " non trovato");
            }
            BookLoan bookLoan = new BookLoan();
            bookLoan.setUser(user);
            bookLoan.setBooks(books);
            return loanRepository.save(bookLoan);
        }else throw new Exception("Problema nella prenotazione dei libri: " + books.stream());
    }
}
