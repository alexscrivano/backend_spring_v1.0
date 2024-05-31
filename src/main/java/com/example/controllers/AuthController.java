package com.example.controllers;

import com.example.entities.User;
import com.example.repositories.UserRepo;
import com.example.security.AccessBody;
import com.example.utils.UserRegistration;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    UserRepo userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> signup(@RequestHeader UserRegistration registration){
        try{
            String serverUrl = "http://localhost:8080/auth/admin/realms/realm_1/users";
            String adminAccessToken = getAdminAccessToken();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(adminAccessToken);
            Map<String,Object> payload = new HashMap<>();
            payload.put("username",registration.getUsername());
            payload.put("email",registration.getEmail());
            payload.put("enabled",true);
            Map<String,Object> cred = new HashMap<>();
            cred.put("type","password");
            cred.put("value",registration.getPassword());
            cred.put("temporary",false);
            payload.put("credentials", Collections.singletonList(cred));

            HttpEntity<Map<String,Object>> req = new HttpEntity<>(payload,headers);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForEntity(serverUrl,req,String.class);

            User u = new User();
            u.setUsername(registration.getUsername());
            u.setEmail(registration.getEmail());
            u.setName(registration.getName());
            u.setSurname(registration.getSurname());
            u.setAddress(registration.getAddress());
            u.setPhoneNumber(registration.getPhone());

            return new ResponseEntity<>(userRepository.save(u),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String getAdminAccessToken() throws Exception{
        String tokenUrl = "http://localhost:8080/auth/realms/realm_1/protocol/openid-connect/token";
        String clientId = "client_1";
        String username = "admin";
        String password = "adminPSW";
        RestTemplate template = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String,String> body = new LinkedMultiValueMap<>();
        body.add("client_id",clientId);
        body.add("username",username);
        body.add("password",password);
        body.add("grant_type","password");

        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(body,headers);
        ResponseEntity<Map> response = template.postForEntity(tokenUrl,request,Map.class);
        if(response.getStatusCode() == HttpStatus.OK && response.getBody() != null){
            return (String) response.getBody().get("access_token");
        }else throw new Exception("Error");
    }
}
