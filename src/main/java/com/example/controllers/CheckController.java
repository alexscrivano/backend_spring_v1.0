package com.example.controllers;

import com.example.entities.User;
import com.example.services.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/check")
public class CheckController {
    @Autowired
    UserAuthService service;

    @GetMapping("/user")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<?> checkUser(){
        try{
            User u = service.sync();
            return new ResponseEntity<>(u, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
