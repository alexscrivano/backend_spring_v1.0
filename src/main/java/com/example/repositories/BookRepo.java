package com.example.repositories;

import com.example.entities.Book;
import com.example.entities.BookLoan;
import com.example.entities.Shelf;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepo extends JpaRepository<Book, Long> {
    List<Book> findByAuthor(String author);
    List<Book> findByTitle(String title);
    List<Book> findByGenre(String genre);
    List<Book> findByTitleAndGenre(String title, String genre);
    List<Book> findByEditor(String editor);
    List<Book> findByShelf(Shelf shelf);
    List<Book> findByTitleAndEditor(String title, String editor);
    List<Book> findByTitleAndAuthor(String title, String author);

    Book findByISBN(String ISBN);

    boolean existsByISBN(String ISBN);
    boolean existsByTitle(String title);
    boolean existsByAuthor(String author);
    boolean existsByGenre(String genre);
    boolean existsByEditor(String editor);

}
