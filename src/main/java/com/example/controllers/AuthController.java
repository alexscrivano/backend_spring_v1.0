package com.example.controllers;

import com.example.security.AccessBody;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody AccessBody accessBody){
        return null;
    }
}
