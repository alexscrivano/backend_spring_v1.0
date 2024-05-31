package com.example.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class UserRegistration {
    String username;
    String password;
    String email;
    String phone;
    String address;
    String name;
    String surname;
}
