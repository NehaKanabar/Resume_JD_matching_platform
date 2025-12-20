package com.resume.resumematching.dto.tenant;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TenantResponse {

    private Long id;
    private String tenantName;
    private String status;
    private String createdAt;
}

