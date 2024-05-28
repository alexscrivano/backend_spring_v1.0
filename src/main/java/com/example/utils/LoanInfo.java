package com.example.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class LoanInfo {
    String userEmail;
    long id;
    String singleISBN;
    String[] ISBNlist;
}
