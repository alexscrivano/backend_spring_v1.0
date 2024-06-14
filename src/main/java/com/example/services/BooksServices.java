package com.example.services;

import com.example.entities.Book;
import com.example.exceptions.BookNotInLibraryException;
import com.example.repositories.BookRepo;
import com.example.repositories.LoanRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class BooksServices {
    @Autowired
    BookRepo bookRepository;
    @Autowired
    LoanRepo loanRepository;

    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    @Transactional(readOnly = true)
    public Book getBookByISBN(String isbn) throws BookNotInLibraryException {
        if(!bookRepository.existsByISBN(isbn)) throw new BookNotInLibraryException("ISBN: " + isbn + " non trovato!");
        return bookRepository.findByISBN(isbn);
    }
    @Transactional(readOnly = true)
    public List<Book> getBookByTitle(String title) throws BookNotInLibraryException {
        if(!bookRepository.existsByTitle(title)) throw new BookNotInLibraryException("Libro: " + title + " non trovato!");
        return bookRepository.findByTitle(title);
    }
    @Transactional(readOnly = true)
    public List<Book> getBookByAuthor(String author) throws BookNotInLibraryException {
        if(!bookRepository.existsByAuthor(author)) throw new BookNotInLibraryException("Autore: " + author + " non trovato!");
        return bookRepository.findByAuthor(author);
    }
    @Transactional(readOnly = true)
    public List<Book> getBookByTitleAndAuthor(String title, String author) throws BookNotInLibraryException {
        if(!bookRepository.existsByTitle(title) || !bookRepository.existsByAuthor(author)) throw new BookNotInLibraryException("Libro: " + title + ", " + author + " non trovato!");
        return bookRepository.findByTitleAndAuthor(title, author);
    }
    @Transactional(readOnly = true)
    public List<Book> getByGenre(String genre) throws BookNotInLibraryException {
        if(!bookRepository.existsByGenre(genre)) throw new BookNotInLibraryException("Genere: " + genre + " non trovato!");
        return bookRepository.findByGenre(genre);
    }
    @Transactional(readOnly = true)
    public List<Book> getByEditor(String editor) throws BookNotInLibraryException {
        if(!bookRepository.existsByEditor(editor)) throw new BookNotInLibraryException("Editore: " + editor + " non trovato!");
        return bookRepository.findByEditor(editor);
    }
    @Transactional(readOnly = true)
    public List<Book> getByTitleAndEditor(String title, String editor) throws BookNotInLibraryException {
        if(!bookRepository.existsByEditor(editor) || !bookRepository.existsByTitle(title)) throw new BookNotInLibraryException("Libro o editore non trovati!");
        return bookRepository.findByTitleAndEditor(title, editor);
    }
    @Transactional(readOnly = true)
    public List<Book> getByTitleAndGenre(String title, String genre) throws BookNotInLibraryException {
        if(!bookRepository.existsByGenre(genre) || !bookRepository.existsByTitle(title)) throw new BookNotInLibraryException("Libro o genere non trovati!");
        return bookRepository.findByTitleAndGenre(title, genre);
    }
    @Transactional(readOnly = true)
    public List<Book> search(String query) throws BookNotInLibraryException {
        Set<Book> books = new HashSet<>();
        if(bookRepository.findByTitleContaining(query) != null) books.addAll(bookRepository.findByTitleContaining(query));
        if(bookRepository.findByAuthorContaining(query) != null) books.addAll(bookRepository.findByAuthorContaining(query));
        if(bookRepository.findByGenreContaining(query) != null) books.addAll(bookRepository.findByGenreContaining(query));
        if(bookRepository.findByEditorContaining(query) != null) books.addAll(bookRepository.findByEditorContaining(query));
        return List.copyOf(books);
    }
}
