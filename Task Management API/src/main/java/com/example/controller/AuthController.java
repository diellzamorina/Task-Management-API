package com.example.controller;

import com.example.dto.AuthentiationRequestDto;
import com.example.dto.AuthenticationResponseDto;
import com.example.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService service;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> login(@RequestBody AuthentiationRequestDto dto){
        return ResponseEntity.ok(service.login(dto));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDto> register(@RequestBody AuthentiationRequestDto dto){
        return ResponseEntity.ok(service.register(dto));
    }
}