package com.resume.resumematching.controller;

import com.resume.resumematching.dto.user.CreateHrUserRequest;
import com.resume.resumematching.dto.user.UserResponse;
import com.resume.resumematching.entity.User;
import com.resume.resumematching.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserResponse> getUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/hr")
    public void createHrUser(@Valid @RequestBody CreateHrUserRequest request) {
        userService.createHrUser(request);
    }
}
