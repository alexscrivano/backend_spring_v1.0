package com.example.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class AccessBody {
    private final String client_id = "client_1";
    private String username;
    private String password;
    private final String grant_type = "password";
}
