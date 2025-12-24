package com.resume.resumematching.tenant;

import com.resume.resumematching.common.ApiResponse;
import com.resume.resumematching.tenant.dto.CreateTenantRequest;
import com.resume.resumematching.tenant.dto.TenantResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    // SUPERUSER → Create tenant + default admin
    @PostMapping
    @PreAuthorize("hasRole('SUPERUSER')")
    public ResponseEntity<ApiResponse<Void>> createTenant(
            @Valid @RequestBody CreateTenantRequest request
    ) {

        tenantService.createTenantWithAdmin(request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Tenant and Admin created successfully",
                        null
                )
        );
    }

    // SUPERUSER → Suspend tenant
    @PatchMapping("/{id}/suspend")
    @PreAuthorize("hasRole('SUPERUSER')")
    public ResponseEntity<ApiResponse<Void>> suspendTenant(
            @PathVariable Long id
    ) {

        tenantService.suspendTenant(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Tenant suspended successfully",
                        null
                )
        );
    }

    // SUPERUSER → Soft delete tenant
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPERUSER')")
    public ResponseEntity<ApiResponse<Void>> deleteTenant(
            @PathVariable Long id
    ) {

        tenantService.deleteTenant(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Tenant deleted successfully",
                        null
                )
        );
    }

    // SUPERUSER → View all tenants
    @GetMapping
    @PreAuthorize("hasRole('SUPERUSER')")
    public ResponseEntity<ApiResponse<List<TenantResponse>>> getAllTenants() {

        List<TenantResponse> tenants = tenantService.getAllTenants();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Tenants fetched successfully",
                        tenants
                )
        );
    }
}
