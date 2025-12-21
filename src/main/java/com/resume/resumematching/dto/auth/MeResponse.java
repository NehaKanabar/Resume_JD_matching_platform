package com.resume.resumematching.dto.auth;

import com.resume.resumematching.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MeResponse {

    private String email;
    private Role role;
    private Long tenantId;
    private String tenantName;
}
