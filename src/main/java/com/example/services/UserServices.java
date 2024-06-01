package com.example.services;

import com.example.entities.Book;
import com.example.entities.BookLoan;
import com.example.entities.User;
import com.example.exceptions.BookNotInLibraryException;
import com.example.exceptions.NoCopiesException;
import com.example.exceptions.UserNotFoundException;
import com.example.repositories.BookRepo;
import com.example.repositories.LoanRepo;
import com.example.repositories.ShelfRepo;
import com.example.repositories.UserRepo;
import com.example.utils.LoanInfo;
import jakarta.persistence.LockModeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class,NoCopiesException.class,UserNotFoundException.class,BookNotInLibraryException.class})
    @Lock(LockModeType.OPTIMISTIC)
    public BookLoan makeALoan(LoanInfo info) throws Exception {
        User user = userRepository.findByEmail(info.getUserEmail());
        List<Book> books = new ArrayList<>();
        for(String isbn : info.getIsbn_list()){
            Book b = bookRepository.findByISBN(isbn);
            books.add(b);
        }
        if(userRepository.existsByEmail(user.getEmail())){
            if(books != null){
                for(Book book : books){
                    if(!bookRepository.existsByISBN(book.getISBN())) throw new BookNotInLibraryException("Libro " + book.getISBN() + " non trovato");
                    if(book.getCopies() <= 0){throw new NoCopiesException("Non ci sono copie disponibili del libro: " + book.getTitle());}
                    int copies = book.getCopies() - 1;
                    book.setCopies(copies);
                }
                BookLoan b = new BookLoan();
                b.setBooks(books);
                b.setUser(user);
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.add(Calendar.DAY_OF_MONTH, 7);
                Date returnDate = c.getTime();
                b.setDateReturn(returnDate);
                userRepository.save(user);
                return loanRepository.save(b);
            }throw new Exception("Problema con i libri che hai richiesto");
        }throw new UserNotFoundException("Utente: " + user.getEmail() + " non trovato");
    }
}
