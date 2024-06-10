package com.example.controllers;

import com.example.services.SyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sync")
public class KeycloakController {
    @Autowired
    SyncService service;

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('user','admin')")
    public ResponseEntity<?> sync() {
        try{
            service.sync();
            return new ResponseEntity<>("Utente sincronizzato", HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
