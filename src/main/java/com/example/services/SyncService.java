package com.example.services;

import com.example.entities.User;
import com.example.repositories.UserRepo;
import com.example.utils.UsersUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SyncService {
    @Autowired
    UserRepo userRepo;
    @Autowired
    UsersUtils utils = new UsersUtils();

    @Transactional
    public void sync(){
        User user = utils.getUser();
        if(userRepo.existsByEmail(user.getEmail()) || userRepo.existsByPhoneNumber(user.getPhoneNumber())){
            User toSync = userRepo.findByEmailOrPhoneNumberOrUsername(user.getEmail(),user.getPhoneNumber(),user.getUsername());
            toSync.setUsername(user.getUsername());
            toSync.setName(user.getName());
            toSync.setSurname(user.getSurname());
            toSync.setPhoneNumber(user.getPhoneNumber());
            toSync.setEmail(user.getEmail());
            toSync.setAddress(user.getAddress());
            userRepo.save(toSync);
        }else{
            userRepo.save(user);
        }
    }
}
