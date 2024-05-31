package com.example.utils;

import com.example.entities.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class UsersUtils {

    public User getUser(){
        JwtAuthenticationToken token = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        Jwt jwtToken = (Jwt)token.getCredentials();
        User usertosync = new User();
        usertosync.setUsername(jwtToken.getClaimAsString("preferred_username"));
        usertosync.setEmail(jwtToken.getClaimAsString("email"));
        usertosync.setName(jwtToken.getClaimAsString("given_name"));
        usertosync.setSurname(jwtToken.getClaimAsString("family_name"));
        usertosync.setPhoneNumber(jwtToken.getClaimAsString("phone_number"));
        usertosync.setAddress(jwtToken.getClaimAsString("address"));

        return usertosync;
    }
}
