package com.resume.resumematching.tenant.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTenantRequest {

    @NotBlank
    private String tenantName;

    @Email
    @NotBlank
    private String adminEmail;

    @NotBlank
    private String adminPassword;
}
