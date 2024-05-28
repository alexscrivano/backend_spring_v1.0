package com.example.repositories;

import com.example.entities.Book;
import com.example.entities.BookLoan;
import com.example.entities.Shelf;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepo extends JpaRepository<Book, Long> {
    List<Book> findByAuthor(String author);
    List<Book> findByTitle(String title);
    List<Book> findByAuthorAndTitle(String author, String title);
    List<Book> findByGenre(String genre);
    List<Book> findByTitleAndGenre(String title, String genre);

    Book findByISBN(String ISBN);
    List<Book> findByShelf(Shelf shelf);

    boolean existsByISBN(String ISBN);


}
