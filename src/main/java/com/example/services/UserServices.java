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

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Lock(LockModeType.OPTIMISTIC)
    public BookLoan makeAsingleLoan(LoanInfo infos) throws Exception{
        if(userRepository.existsByEmail(infos.getUserEmail())){
            if(bookRepository.existsByISBN(infos.getBook_isbn())){
                User user = userRepository.findByEmail(infos.getUserEmail());
                Book book = bookRepository.findByISBN(infos.getBook_isbn());
                if(book != null){
                    if(book.getCopies() > 0){
                        BookLoan loan = createLoan(book,user);
                        int copies = book.getCopies() - 1;
                        book.setCopies(copies);
                        return loanRepository.save(loan);
                    }else throw new NoCopiesException("Non ci sono copie disponibili del libro: " + book.getTitle());
                }else throw new Exception("Libro: " + infos.getBook_isbn() + " non trovato");
            }else throw new BookNotInLibraryException("Libro: " + infos.getBook_isbn() + " non presente nella biblioteca");
        }else throw new UserNotFoundException("Utente: " + infos.getUserEmail() + " non trovato");
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Lock(LockModeType.OPTIMISTIC)
    public BookLoan makeALoan(User user, List<Book> books) throws Exception {
        if(userRepository.existsByEmail(user.getEmail())){
            if(books != null){
                for(Book book : books){
                    if(!bookRepository.existsByISBN(book.getISBN())) throw new BookNotInLibraryException("Libro " + book.getISBN() + " non trovato");
                    if(book == null) throw new BookNotInLibraryException("Libro " + book.getISBN() + " non trovato");
                    if(book.getCopies() <= 0){throw new NoCopiesException("Non ci sono copie disponibili del libro: " + book.getTitle());}
                    int copies = book.getCopies() - 1;
                    book.setCopies(copies);
                }
                BookLoan loan = createLoan(user,books);
                return loanRepository.save(loan);
            }else throw new Exception("Problema con i libri che hai richiesto");
        }else throw new UserNotFoundException("Utente: " + user.getEmail() + " non trovato");
    }

    private BookLoan createLoan(Book book, User user){
        BookLoan bookLoan = new BookLoan();
        bookLoan.setUser(user);
        Book b = new Book(book);
        b.setCopies(1);
        bookLoan.setBooks(List.of(book));
        return bookLoan;
    }
    private BookLoan createLoan(User user, List<Book> books){
        BookLoan bookLoan = new BookLoan();
        bookLoan.setUser(user);
        List<Book> copy = List.copyOf(books);
        List<Book> books1 = new ArrayList<>();
        for(Book book : copy){
            Book b = new Book(book);
            b.setCopies(1);
            books1.add(b);
        }
        bookLoan.setBooks(books1);
        return bookLoan;
    }
}
