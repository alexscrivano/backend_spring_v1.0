package com.example.repositories;

import com.example.entities.Book;
import com.example.entities.BookLoan;
import com.example.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByPhoneNumber(String phone_number);
    User findById(long id);

    List<User> findByName(String name);
    List<User> findByAddress(String address);
    List<User> findBySurname(String surname);
}
