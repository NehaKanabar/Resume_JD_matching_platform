package com.resume.resumematching.user.dto;

import com.resume.resumematching.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private Long id;
    private String email;
    private Role role;
    private boolean disabled;
    private Long tenantId;
    private LocalDateTime createdAt;
}
