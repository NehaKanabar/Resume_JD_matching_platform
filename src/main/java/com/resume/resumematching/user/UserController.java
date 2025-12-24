package com.resume.resumematching.user;

import com.resume.resumematching.common.ApiResponse;
import com.resume.resumematching.user.dto.CreateHrUserRequest;
import com.resume.resumematching.user.dto.UserResponse;
import com.resume.resumematching.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // SUPERUSER → all users
    // ADMIN → users of own tenant
    @GetMapping
    @PreAuthorize("hasAnyRole('SUPERUSER','ADMIN')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getUsers() {

        List<UserResponse> users = userService.getAllUsers();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Users fetched successfully",
                        users
                )
        );
    }

    // ADMIN → create HR user
    @PostMapping("/hr")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> createHrUser(
            @Valid @RequestBody CreateHrUserRequest request
    ) {

        userService.createHrUser(request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "HR user created successfully",
                        null
                )
        );
    }
}
