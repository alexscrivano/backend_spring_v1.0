package com.example;

import com.example.entities.BookLoan;
import com.example.repositories.LoanRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
class RemoveLoans extends Thread{
    @Autowired
    LoanRepo loanRepo;

    @Override
    public void run() {
        try{
            while(true){
                TimeUnit.DAYS.sleep(1);
                List<BookLoan> loans = loanRepo.findAll();
                for(BookLoan loan : loans){
                    if(loan.isReturned()) loanRepo.delete(loan);
                }
            }
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}

@SpringBootApplication
public class PswProjectTry1Application {

    public static void main(String[] args) {

        SpringApplication.run(PswProjectTry1Application.class, args);
    }

}
