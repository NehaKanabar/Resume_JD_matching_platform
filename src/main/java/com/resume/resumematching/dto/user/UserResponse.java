package com.resume.resumematching.dto.user;

import com.resume.resumematching.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String email;
    private Role role;
    private boolean disabled;
    private Long tenantId;   // IMPORTANT
    private LocalDateTime createdAt;
}
