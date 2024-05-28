package com.example.repositories;

import com.example.entities.Book;
import com.example.entities.Shelf;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShelfRepo extends JpaRepository<Shelf, Long> {
    List<Book> findBooksById(Long id);
}
