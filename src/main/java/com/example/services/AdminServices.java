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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public BookLoan deleteLoan(Long num_loan) throws Exception {
        if(loanRepo.existsById(num_loan)){
            BookLoan bookLoan = loanRepo.findById(num_loan).get();
            List<Book> books = bookLoan.getBooks();
            if(books.isEmpty()) throw new Exception("Problema con la cancellazione del prestito");
            for(Book book : books){
                Book dbBook = bookRepo.findByISBN(book.getISBN());
                dbBook.setCopies(dbBook.getCopies() + 1);
            }
            loanRepo.deleteById(num_loan);
            return bookLoan;
        }else throw new Exception("Prestito numero " + num_loan + " non trovato");

    }

    public Map<String,List<BookLoan>> allPrestiti(){
        Map<String,List<BookLoan>> prestiti = new HashMap<>();
        for(User user : userRepo.findAll()){
            prestiti.putIfAbsent(user.getEmail(), user.getLoanList());
        }
        return prestiti;
    }

    public void returned(Long num_loan) throws Exception {
        if(!loanRepo.existsById(num_loan)) throw new Exception("Prestito non trovato");
        loanRepo.deleteById(num_loan);
    }

}
