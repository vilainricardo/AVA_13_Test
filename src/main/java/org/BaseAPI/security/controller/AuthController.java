package org.BaseAPI.security.controller;

import jakarta.validation.Valid;
import org.BaseAPI.erros.exceptions.ExpiredSessionException;
import org.BaseAPI.security.model.LoginRequestDTO;
import org.BaseAPI.security.model.LoginResposeDTO;
import org.BaseAPI.security.Services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/auth/login")
    public LoginResposeDTO login(@Valid @RequestBody LoginRequestDTO request) {
        return authService.login( request.getEmail(), request.getPassword() );
    }

    @PostMapping("/auth/refresh")
    public String refresh(@RequestHeader(value = "Authorization", required = false) String authorization) {

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new ExpiredSessionException();
        }
        String refreshToken = authorization.substring("Bearer ".length()).trim();

        // Validate refresh token
        return authService.refreshToken( refreshToken );

    }
}
