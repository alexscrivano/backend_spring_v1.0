package com.example.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class LoanInfo {
    String userEmail;
    String[] isbn_list;

    public LoanInfo(){}

    public LoanInfo(String userEmail, String[] isbn_list) {
        this.userEmail = userEmail;
        this.isbn_list = isbn_list;
    }
}
