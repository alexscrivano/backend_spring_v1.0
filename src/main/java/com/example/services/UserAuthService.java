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
    public User sync(){
        User user = utils.getUser();
        return userRepo.save(user);
    }
}
