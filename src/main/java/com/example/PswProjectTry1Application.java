package com.example;

import com.example.entities.BookLoan;
import com.example.repositories.LoanRepo;
import com.example.services.AdminServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
class DeleteLoans extends Thread{
    @Autowired
    AdminServices adminServices;
    @Autowired
    LoanRepo loanRepo;
    @Override
    public void run() {
        try{
            while(true){
                TimeUnit.DAYS.sleep(1);
                List<BookLoan> loans = loanRepo.findAll();
                for(BookLoan loan : loans){
                    if(loan.getDateReturn().before(new Date())){
                        adminServices.deleteLoan(loan.getNumLoan());
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

@SpringBootApplication
public class PswProjectTry1Application {
    public static void main(String[] args) {SpringApplication.run(PswProjectTry1Application.class, args);}
}
