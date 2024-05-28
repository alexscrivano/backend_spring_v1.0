package com.example.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "shelf",schema = "library")
public class Shelf {
    @Id
    private long id;

    @OneToMany(mappedBy = "shelf", fetch = FetchType.LAZY)
    @JsonManagedReference
    public List<Book> books = new ArrayList<>();

    public void addBook(Book book) {
        this.books.add(book);
        book.setShelf(this);
    }
}
