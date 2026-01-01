package com.resume.resumematching.auth;

import com.resume.resumematching.auth.dto.LoginRequest;
import com.resume.resumematching.auth.dto.LoginResponse;
import com.resume.resumematching.auth.dto.MeResponse;
import com.resume.resumematching.common.ApiResponse;
import com.resume.resumematching.user.entity.User;
import com.resume.resumematching.exception.ResourceNotFoundException;
import com.resume.resumematching.user.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
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
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Long tenantId = user.getTenant() != null
                ? user.getTenant().getId()
                : null;

        String tenantName = user.getTenant() != null
                ? user.getTenant().getName()
                : null;

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole(),
                tenantId
        );

        Cookie jwtCookie = new Cookie("JWT_TOKEN", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(24 * 60 * 60);

        response.addCookie(jwtCookie);

        LoginResponse loginResponse = new LoginResponse(
                user.getEmail(),
                user.getRole(),
                tenantId,
                tenantName
        );

        return ResponseEntity.ok(
                ApiResponse.success("Login successful", loginResponse)
        );
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MeResponse>> me() {

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

        MeResponse meResponse = new MeResponse(
                user.getEmail(),
                user.getRole(),
                tenantId,
                tenantName
        );

        return ResponseEntity.ok(
                ApiResponse.success("User details fetched", meResponse)
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletResponse response) {

        Cookie cookie = new Cookie("JWT_TOKEN", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);

        return ResponseEntity.ok(
                ApiResponse.success("Logged out successfully", null)
        );
    }
}