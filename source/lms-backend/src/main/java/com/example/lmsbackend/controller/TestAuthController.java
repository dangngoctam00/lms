package com.example.lmsbackend.controller;

import com.example.lmsbackend.config.security.CustomUserDetails;
import com.example.lmsbackend.service.TestAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class TestAuthController {

    @Autowired
    private TestAuthService service;

    @GetMapping("testAuth/{id}")
    public ResponseEntity<String> testAuth(@PathVariable(name = "id") int id) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(new CustomUserDetails(1L,"a", null), null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        return ResponseEntity.ok(service.testAuth(id));
    }

    @GetMapping("testAuth")
    public ResponseEntity<String> testAuth() {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(new CustomUserDetails(1L,"a", null), null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        return ResponseEntity.ok(service.testAuth());
    }
}
