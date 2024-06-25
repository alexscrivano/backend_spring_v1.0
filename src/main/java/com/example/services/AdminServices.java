package com.example.services;

import com.example.entities.Book;
import com.example.entities.BookLoan;
import com.example.entities.Shelf;
import com.example.entities.User;
import com.example.exceptions.UserAlreadyExistsException;
import com.example.repositories.BookRepo;
import com.example.repositories.LoanRepo;
import com.example.repositories.ShelfRepo;
import com.example.repositories.UserRepo;
import com.example.utils.LoanDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AdminServices {
    @Autowired
    BookRepo bookRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    LoanRepo loanRepo;
    @Autowired
    ShelfRepo shelfRepo;

    public Book addBook(Book book, long shelf) throws Exception {
        if(!bookRepo.existsByISBN(book.getISBN()) && shelfRepo.existsById(shelf)){
            System.out.println("Scaffale trovato: " + shelfRepo.findById(shelf).get());
            List<Book> books = new ArrayList<>();
            books.add(book);
            shelfRepo.findById(shelf).get().addBook(book);
            System.out.println("Libro aggiunto allo scaffale");
            return bookRepo.save(book);
        }else{
            throw new Exception("Problema nell'inserimento del libro: " + book.getTitle() + " nello scaffale: " + shelf);
        }
    }
    public User addUser(User user) throws Exception {
        if(!userRepo.existsByEmail(user.getEmail())){
            if(userRepo.existsByPhoneNumber(user.getPhoneNumber())) throw new UserAlreadyExistsException("Il numero che stai usando é già usato da un'altro utente");
            return userRepo.save(user);
        }
        else throw new Exception("Problema nell'inserimento dell'utente: " + user.getName() + user.getSurname());
    }
    public Shelf addShelf(Shelf shelf) throws Exception {
        return shelfRepo.save(shelf);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteLoan(Long num_loan) throws Exception {
        if(loanRepo.existsById(num_loan)){
            BookLoan loan = loanRepo.findById(num_loan).get();
            List<Book> books = loan.getBooks();
            for(Book book : books){
                book.setCopies(book.getCopies() + 1);
            }
            loanRepo.delete(loan);
        }else throw new Exception("Prestito numero " + num_loan + " non trovato");
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void confirmLoan(Long num_loan) throws Exception {
        if(loanRepo.existsById(num_loan)){
            loanRepo.findByNumLoan(num_loan).setConfirmed(true);
        }else{
            throw new Exception("Prestito numero " + num_loan + " non trovato");
        }
    }

}
