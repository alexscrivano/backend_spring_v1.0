package com.example.controllers;

import com.example.entities.Book;
import com.example.entities.BookLoan;
import com.example.entities.Shelf;
import com.example.entities.User;
import com.example.repositories.BookRepo;
import com.example.repositories.LoanRepo;
import com.example.repositories.ShelfRepo;
import com.example.services.AdminServices;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    BookRepo bookRepo;
    @Autowired
    LoanRepo loanRepo;
    @Autowired
    ShelfRepo shelfRepo;
    @Autowired
    AdminServices adminServices;

    @GetMapping("/books")
    public ResponseEntity<?> allBooks() {
        return new ResponseEntity<>(bookRepo.findAll(), HttpStatus.OK);
    }
    @GetMapping("/shelves")
    public ResponseEntity<?> allShelves() {
        return new ResponseEntity<>(shelfRepo.findAll(), HttpStatus.OK);
    }
    @GetMapping("/booksByShelf")
    public ResponseEntity<?> allBooksByShelf(@RequestBody Shelf shelf) {
        return new ResponseEntity<>(bookRepo.findByShelf(shelf), HttpStatus.OK);
    }
    @PostMapping("/admin/addBook-{shelf}")
    public ResponseEntity<?> addBook(@PathVariable long shelf, @RequestBody Book book) {
        try {
            Book b = adminServices.addBook(book,shelf);
            return new ResponseEntity<>(b, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/admin/addUser")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        try{
            User u = adminServices.addUser(user);
            return new ResponseEntity<>(u, HttpStatus.OK);
        }catch (Exception e) {
            System.err.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/admin/addShelf")
    public ResponseEntity<?> addShelf(@RequestBody Shelf shelf) {
        try{
            Shelf s = adminServices.addShelf(shelf);
            return new ResponseEntity<>(s, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/admin/deleteLoan1")
    public ResponseEntity<?> deleteLoan1(@RequestBody BookLoan bookLoan) {
        adminServices.deleteLoan(bookLoan);
        return new ResponseEntity<>("Prestito: " + bookLoan.getNumLoan() + " cancellato", HttpStatus.OK);
    }
    @DeleteMapping("/admin/deleteLoan2")
    public ResponseEntity<?> deleteLoan2(@RequestBody User user, long num_loan) {
        adminServices.deleteLoan(user, num_loan);
        return new ResponseEntity<>("Prestito: " + num_loan + " cancellato", HttpStatus.OK);
    }
    @DeleteMapping("/admin/deleteLoan3")
    public ResponseEntity<?> deleteLoan3(@RequestBody long num_loan) {
        adminServices.deleteLoan(num_loan);
        return new ResponseEntity<>("Prestito: " + num_loan + " cancellato", HttpStatus.OK);
    }

    @PostMapping("/makeAloan1")
    public ResponseEntity<?> makeAloan1(@RequestBody User user, Book book) {
        BookLoan loan = new BookLoan();
        loan.setUser(user);
        loan.setBooks(new LinkedList<>(List.of(book)));
        BookLoan added = loanRepo.save(loan);
        return new ResponseEntity<>(added, HttpStatus.OK);
    }
    @PostMapping("/makeAloan2")
    public ResponseEntity<?> makeAloan2(@RequestBody User user, List<Book> books) {
        BookLoan loan = new BookLoan();
        loan.setUser(user);
        loan.setBooks(books);
        BookLoan added = loanRepo.save(loan);
        return new ResponseEntity<>(added, HttpStatus.OK);
    }
}
