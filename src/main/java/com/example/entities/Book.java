package com.example.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "books",schema = "library")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "ISBN")
    private String ISBN;

    @Column(name = "title")
    private String title;
    @Column(name = "author")
    private String author;
    @Column(name = "genre")
    private String genre;
    @Column(name = "editor_name")
    private String editor;
    @Column(name = "copies")
    private int copies;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    Shelf shelf;

    public Book() {}
    public Book(Book b){
        this.id = b.id;
        this.ISBN = b.getISBN();
        this.title = b.getTitle();
        this.author = b.getAuthor();
        this.genre = b.getGenre();
        this.editor = b.getEditor();
        this.copies = b.getCopies();
        this.shelf = b.getShelf();
    }

}
