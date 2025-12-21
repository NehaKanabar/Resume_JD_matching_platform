package com.resume.resumematching.controller;

import com.resume.resumematching.dto.auth.LoginRequest;
import com.resume.resumematching.dto.auth.LoginResponse;
import com.resume.resumematching.dto.auth.MeResponse;
import com.resume.resumematching.entity.User;
import com.resume.resumematching.enums.Role;
import com.resume.resumematching.repository.UserRepository;
import com.resume.resumematching.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Long tenantId = user.getTenant() != null
                ? user.getTenant().getId()
                : null;

        String tenantName = user.getTenant() != null
                ? user.getTenant().getName()
                : null; // SUPERUSER

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole(),
                tenantId
        );

        Cookie jwtCookie = new Cookie("JWT_TOKEN", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false); // true in prod
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(24 * 60 * 60);

        response.addCookie(jwtCookie);

        return ResponseEntity.ok(
                new LoginResponse(
                        "Login successful",
                        user.getEmail(),
                        user.getRole(),
                        tenantId,
                        tenantName
                )
        );
    }

    @GetMapping("/me")
    public ResponseEntity<MeResponse> me() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long tenantId = user.getTenant() != null
                ? user.getTenant().getId()
                : null;

        String tenantName = user.getTenant() != null
                ? user.getTenant().getName()
                : null;

        return ResponseEntity.ok(
                new MeResponse(
                        user.getEmail(),
                        user.getRole(),
                        tenantId,
                        tenantName
                )
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {

        Cookie cookie = new Cookie("JWT_TOKEN", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // true in prod
        cookie.setPath("/");
        cookie.setMaxAge(0); // delete immediately

        response.addCookie(cookie);

        return ResponseEntity.ok("Logged out successfully");
    }

}

