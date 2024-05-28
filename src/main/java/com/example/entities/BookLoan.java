package com.example.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "loans",schema = "library")
public class BookLoan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long numLoan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ISBN")
    List<Book> books = new ArrayList<>();
}
