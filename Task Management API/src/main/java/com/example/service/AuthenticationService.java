package com.example.service;

import com.example.dto.AuthentiationRequestDto;
import com.example.dto.AuthenticationResponseDto;
import com.example.exceptions.UserNotFoundException;
import com.example.model.Role;
import com.example.model.User;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtImplService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthenticationResponseDto register(AuthentiationRequestDto dto) {
        var user = User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .lastName(dto.getLastName())
                .firstName(dto.getFirstName())
                .role(Role.USER)
                .build();
        user = userRepository.save(user);
        return AuthenticationResponseDto.builder()
                .token(jwtService.generateToken(user))
                .email(user.getEmail())
                .build();
    }

    public AuthenticationResponseDto login(AuthentiationRequestDto dto) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    dto.getEmail(),
                    dto.getPassword()
            ));

            var user = userRepository.findByEmail(dto.getEmail());
            if (user.isPresent()) {
                return AuthenticationResponseDto.builder()
                        .token(jwtService.generateToken(user.get()))
                        .email(user.get().getEmail())
                        .build();
            }else {
                throw new UserNotFoundException("User not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserNotFoundException("Bad credentials");
        }
    }


}