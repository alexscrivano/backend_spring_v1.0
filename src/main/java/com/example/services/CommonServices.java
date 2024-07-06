package com.example.services;

import com.example.entities.Book;
import com.example.entities.BookLoan;
import com.example.entities.User;
import com.example.repositories.LoanRepo;
import com.example.repositories.UserRepo;
import com.example.utils.LoanDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CommonServices {
    @Autowired
    UserRepo userRepo;
    @Autowired
    private LoanRepo loanRepo;

    public Map<String, List<LoanDTO>> allLoans(){
        Map<String,List<LoanDTO>> prestiti = new HashMap<>();
        for(User user : userRepo.findAll()){
                List<LoanDTO> list = new ArrayList<>();
                for(BookLoan loan : user.getLoanList()){
                    LoanDTO dto = new LoanDTO();
                    dto.setNum_loan(loan.getNumLoan());
                    dto.setDate(loan.getDate());
                    dto.setReturnDate(loan.getDateReturn());
                    dto.setBooks(loan.getBooks());
                    list.add(dto);
                }
            prestiti.putIfAbsent(user.getEmail(), list);
        }
        return prestiti;
    }

    public Map<String,List<LoanDTO>> loansByUserEmail(String email){
        Map<String,List<LoanDTO>> prestiti = new HashMap<>();
        User u = userRepo.findByEmail(email);
        List<LoanDTO> list = new ArrayList<>();
        for(BookLoan loan : u.getLoanList()){
            LoanDTO dto = new LoanDTO();
            dto.setNum_loan(loan.getNumLoan());
            dto.setDate(loan.getDate());
            dto.setReturnDate(loan.getDateReturn());
            dto.setBooks(loan.getBooks());
            list.add(dto);
        }
        prestiti.putIfAbsent(u.getEmail(), list);
        return prestiti;
    }

    public List<Book> loansByNum(long num){
        List<Book> books = new ArrayList<>();
        for(Book b : loanRepo.findByNumLoan(num).getBooks()){
            books.add(b);
        }
        Collections.reverse(books);
        return books;
    }
}
