package com.example.controllers;

import com.example.entities.User;
import com.example.repositories.UserRepo;
import com.example.utils.JwtConverter;
import com.example.utils.UsersUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/get")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    @Autowired
    UserRepo userRepo;
    @Autowired
    UsersUtils utils;

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('admin','user')")
    public ResponseEntity<?> getUser(){
        try{
            User u = utils.getUser();
            return new ResponseEntity<>(u, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
