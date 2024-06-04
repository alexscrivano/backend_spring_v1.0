package com.example.controllers;

import com.example.entities.Book;
import com.example.entities.BookLoan;
import com.example.entities.Shelf;
import com.example.entities.User;
import com.example.exceptions.BookNotInLibraryException;
import com.example.repositories.BookRepo;
import com.example.repositories.UserRepo;
import com.example.services.AdminServices;
import com.example.services.BooksServices;
import com.example.services.CommonServices;
import com.example.services.UserServices;
import com.example.utils.LoanInfo;
import com.example.utils.UsersUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/library")
@CrossOrigin(origins = "http://localhost:4200")
public class LibraryController {
    @Autowired
    AdminServices adminServices;
    @Autowired
    UserServices userServices;
    @Autowired
    BooksServices bookServices;
    @Autowired
    CommonServices commonServices;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private BookRepo bookRepo;
    @Autowired
    private UsersUtils usersUtils;

    /*GET REQUESTS
        - Admin requests ()
        - User requests ()
     */
    @GetMapping("/books/allBooks")
    @PreAuthorize("hasAnyRole('user','admin')")
    public ResponseEntity<?> getAllBooks() {
        return new ResponseEntity<>(bookServices.getAllBooks(), HttpStatus.OK);
    }
    @GetMapping("/books/bookSearch")
    @PreAuthorize("hasAnyRole('user','admin')")
    public ResponseEntity<?> getBookSearchISBN(@RequestParam String searchQuery, @RequestParam String search_type) {
        try{
            List<Book> searchedBooks;
            switch (search_type) {
                case "ISBN": searchedBooks = List.of(bookRepo.findByISBN(searchQuery));
                case "Author": searchedBooks = bookRepo.findByAuthorContaining(searchQuery);
                default: searchedBooks = bookRepo.findByTitleContaining(searchQuery);
            }
            return new ResponseEntity<>(searchedBooks, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
//    @GetMapping("/books/bookSearchTitle")
//    @PreAuthorize("hasAnyRole('user','admin')")
//    public ResponseEntity<?> getBookSearchTitle(@RequestParam String book_title) {
//        try{
//            List<Book> books = bookServices.getBookByTitle(book_title);
//            return new ResponseEntity<>(books, HttpStatus.OK);
//        }catch (BookNotInLibraryException e){
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
//        }
//    }
//    @GetMapping("/books/bookSearchAuthor")
//    @PreAuthorize("hasAnyRole('user','admin')")
//    public ResponseEntity<?> getBookSearchAuthor(@RequestParam String book_author) {
//        try{
//            List<Book> books = bookServices.getBookByAuthor(book_author);
//            return new ResponseEntity<>(books, HttpStatus.OK);
//        }catch (BookNotInLibraryException e){
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
//        }
//    }
    @GetMapping("/books/bookByTitleAndAuthor")
    @PreAuthorize("hasAnyRole('user','admin')")
    public ResponseEntity<?> getBookByTitleAndAuthor(@RequestParam String book_title, @RequestParam String author) {
        try{
            List<Book> books = bookServices.getBookByTitleAndAuthor(book_title,author);
            return new ResponseEntity<>(books, HttpStatus.OK);
        }catch (BookNotInLibraryException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/books/bookByGenre")
    @PreAuthorize("hasAnyRole('user','admin')")
    public ResponseEntity<?> getBookByGenre(@RequestParam String genre) {
        try{
            List<Book> books = bookServices.getByGenre(genre);
            return new ResponseEntity<>(books, HttpStatus.OK);
        }catch (BookNotInLibraryException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/books/bookByEditor")
    @PreAuthorize("hasAnyRole('user','admin')")
    public ResponseEntity<?> getBookByEditor(@RequestParam String editor) {
        try{
            List<Book> books = bookServices.getByEditor(editor);
            return new ResponseEntity<>(books, HttpStatus.OK);
        }catch (BookNotInLibraryException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/books/bookByTitleAndEditor")
    @PreAuthorize("hasAnyRole('user','admin')")
    public ResponseEntity<?> getBookByTitleAndEditor(@RequestParam String title, @RequestParam String editor) {
        try{
            List<Book> books = bookServices.getByTitleAndEditor(title,editor);
            return new ResponseEntity<>(books,HttpStatus.OK);
        }catch (BookNotInLibraryException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/books/bookByTitleAndGenre")
    @PreAuthorize("hasAnyRole('user','admin')")
    public ResponseEntity<?> getBookByTitleAndGenre(@RequestParam String title, @RequestParam String genre) {
        try{
            List<Book> books = bookServices.getByTitleAndGenre(title,genre);
            return new ResponseEntity<>(books,HttpStatus.OK);
        }catch (BookNotInLibraryException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/loans/allLoans")
    @PreAuthorize("hasAnyRole('user','admin')")
    public ResponseEntity<?> getAllLoans() {
        try{
            return new ResponseEntity<>(commonServices.allLoans(), HttpStatus.OK);
            //return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(commonServices.allLoans());
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/loans/allLoansByUseremail")
    @PreAuthorize("hasAnyRole('user','admin')")
    public ResponseEntity<?> getAllLoansByUser(@RequestParam String email) {
        try{
            return new ResponseEntity<>(commonServices.loansByUserEmail(email), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /*POST REQUESTS
    *   - Admin requests (addUser, addBooksOnShelf, addShelf)
    *   - User requests (make a loan)
    * */
    @PostMapping("/admin/addUser")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        try{
            adminServices.addUser(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/admin/addBookOn{shelf}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> addBookOn(@PathVariable Long shelf, @RequestBody Book book) {
        try{
            Book b = adminServices.addBook(book,shelf);
            return new ResponseEntity<>(b, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/admin/addShelf")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> addShelf(@RequestBody Shelf shelf) {
        try{
            Shelf s = adminServices.addShelf(shelf);
            return new ResponseEntity<>(s, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/user/loanRequest")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<?> requestALoan(@RequestBody LoanInfo info){
        try{
            if(!usersUtils.verifyToken(info.getUserEmail())) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            User u = userRepo.findByEmail(info.getUserEmail());
            List<Book> books = new ArrayList<>();
            if(info.getIsbn_list().length == 0) return new ResponseEntity<>("Impossibile confermare un prestito vuoto",HttpStatus.BAD_REQUEST);
            for(String s : info.getIsbn_list()){
                Book b = bookRepo.findByISBN(s);
                books.add(b);
            }
            BookLoan b = userServices.makeALoan(u,books);
            return new ResponseEntity<>(b, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /*DELETE REQUESTS
        - Admin requests (deleteLoan)
        - User requests ()
     */
    @DeleteMapping("/admin/returnLoan")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> returnLoan(@RequestParam Long num_loan){
        try{
            adminServices.deleteLoan(num_loan);
            return new ResponseEntity<>("Prestito restituito", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("Problema con l'annullamento del prestito: " + num_loan + ", " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
