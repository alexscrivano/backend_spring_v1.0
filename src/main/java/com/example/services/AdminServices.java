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
import java.util.List;

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
    public void deleteLoan(BookLoan loan){
        loanRepo.delete(loan);
    }
    public void deleteLoan(User user, long num_loan){
        List<BookLoan> loans = loanRepo.findByUser(user);
        for(BookLoan loan : loans) {
            if(loan.getNumLoan() == num_loan) {
                loanRepo.delete(loan);
            }
        }
    }
    public void deleteLoan(long num_loan){
        BookLoan loan = loanRepo.findByNumLoan(num_loan);
        deleteLoan(loan);
    }
}
