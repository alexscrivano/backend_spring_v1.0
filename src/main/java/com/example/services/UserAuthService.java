package com.example.services;

import com.example.entities.User;
import com.example.exceptions.UserNotFoundException;
import com.example.repositories.UserRepo;
import com.example.utils.UsersUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserAuthService {
    @Autowired
    UserRepo userRepo;
    @Autowired
    UsersUtils utils = new UsersUtils();

    @Transactional
    public User sync() throws Exception{
        User user = utils.getUser();
        if(!userRepo.existsByEmail(user.getEmail())) return userRepo.save(user);
        else throw new UserNotFoundException("Utente non trovato");
    }
}
