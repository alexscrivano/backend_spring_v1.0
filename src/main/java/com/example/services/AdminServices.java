package com.example.services;

import com.example.entities.Book;
import com.example.entities.BookLoan;
import com.example.entities.Shelf;
import com.example.entities.User;
import com.example.repositories.BookRepo;
import com.example.repositories.LoanRepo;
import com.example.repositories.ShelfRepo;
import com.example.repositories.UserRepo;
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
        if(!userRepo.existsByEmail(user.getEmail())){ return userRepo.save(user);}
        else throw new Exception("Problema nell'inserimento dell'utente: " + user.getName() + user.getSurname());
    }
    public Shelf addShelf(Shelf shelf) throws Exception {
        return shelfRepo.save(shelf);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteLoan(Long num_loan) throws Exception {
        if(loanRepo.existsById(num_loan)){
            BookLoan loan = loanRepo.findById(num_loan).get();
            if(loan.getDateReturn().before(new Date())) throw new Exception("Prestito scaduto");
            List<Book> books = loan.getBooks();
            for(Book book : books){
                book.setCopies(book.getCopies() + 1);
            }
            loanRepo.delete(loan);
        }else throw new Exception("Prestito numero " + num_loan + " non trovato");

    }

    public Map<String,List<BookLoan>> allPrestiti(){
        Map<String,List<BookLoan>> prestiti = new HashMap<>();
        for(User user : userRepo.findAll()){
            prestiti.putIfAbsent(user.getEmail(), user.getLoanList());
        }
        return prestiti;
    }


}
