package com.example.controllers;

import com.example.entities.Book;
import com.example.entities.BookLoan;
import com.example.entities.Shelf;
import com.example.entities.User;
import com.example.repositories.BookRepo;
import com.example.repositories.LoanRepo;
import com.example.repositories.ShelfRepo;
import com.example.repositories.UserRepo;
import com.example.services.AdminServices;
import com.example.services.UserServices;
import com.example.utils.LoanInfo;
import jakarta.websocket.server.PathParam;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    @Autowired
    UserServices userServices;
    @Autowired
    private UserRepo userRepo;

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
    @GetMapping("/bookByISBN")
    public ResponseEntity<?> bookByISBN(@RequestParam String isbn) {
        return new ResponseEntity<>(bookRepo.findByISBN(isbn), HttpStatus.OK);
    }
    @GetMapping("/allLoans")
    public ResponseEntity<?> allLoans() {
        return new ResponseEntity<>(adminServices.allPrestiti(), HttpStatus.OK);
    }
    @GetMapping("/users")
    public ResponseEntity<?> allUsers() {
        return new ResponseEntity<>(userRepo.findAll(), HttpStatus.OK);
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
    @PostMapping("/loans/makeALoan1")
    public ResponseEntity<?> makeABookLoan(@RequestBody LoanInfo infos){
        try{
            User u = userRepo.findByEmail(infos.getUserEmail());
            List<Book> books = new ArrayList<>();
            System.out.println(Arrays.toString(infos.getIsbn_list()));
            for(String s : infos.getIsbn_list()){
                Book b = bookRepo.findByISBN(s);
                books.add(b);
            }
            BookLoan b = userServices.makeALoan(u,books);
            return new ResponseEntity<>(b, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/admin/returned")
    public ResponseEntity<?> returnBook(@RequestParam Long num_loan){
        try{
            adminServices.returned(num_loan);
            return new ResponseEntity<>("Prestito restituito",HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/admin/deleteLoan")
    public ResponseEntity<?> deleteLoan(@RequestParam Long num_loan){
        try {
            BookLoan b = adminServices.deleteLoan(num_loan);
            return new ResponseEntity<>(b, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
