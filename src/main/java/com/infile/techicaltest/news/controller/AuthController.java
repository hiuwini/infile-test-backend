package com.infile.techicaltest.news.controller;

import com.infile.techicaltest.news.dto.AuthRequest;
import com.infile.techicaltest.news.dto.AuthResponse;
import com.infile.techicaltest.news.dto.RegisterResponse;
import com.infile.techicaltest.news.entity.Usuario;
import com.infile.techicaltest.news.service.impl.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Validated @RequestBody AuthRequest request) {
        AuthResponse response = authService.authenticate(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody Usuario usuario) {
        RegisterResponse nuevo = authService.register(usuario);
        return ResponseEntity.ok(nuevo);
    }

}
