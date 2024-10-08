package com.example.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "loans",schema = "library")
public class BookLoan {
    final int DAY = 1000 * 60 * 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long numLoan;

    @CreationTimestamp
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(name = "returnDate")
    @Temporal(TemporalType.DATE)
    private Date dateReturn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    User user;


    @Column(name = "confirmed")
    @JsonIgnore
    private boolean confirmed;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "books_in_loan",
            joinColumns = @JoinColumn(name = "loan_id"),
            inverseJoinColumns = @JoinColumn(name = "book_isbn")
    )
    List<Book> books = new ArrayList<>();

}
