package com.resume.resumematching.controller;

import com.resume.resumematching.dto.auth.LoginRequest;
import com.resume.resumematching.dto.auth.LoginResponse;
import com.resume.resumematching.entity.User;
import com.resume.resumematching.enums.Role;
import com.resume.resumematching.repository.UserRepository;
import com.resume.resumematching.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {

        // Authenticate credentials
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Fetch full user from DB
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found")
                );

        Long tenantId = (user.getTenant() != null)
                ? user.getTenant().getId()
                : null;

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole(),
                tenantId
        );

        return ResponseEntity.ok(
                new LoginResponse("Login successful", token)
        );
    }
}
