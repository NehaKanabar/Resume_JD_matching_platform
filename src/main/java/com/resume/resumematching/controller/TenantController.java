package com.resume.resumematching.controller;

import com.resume.resumematching.dto.tenant.CreateTenantRequest;
import com.resume.resumematching.service.TenantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    // SUPERUSER creates tenant + admin
    @PostMapping
    @PreAuthorize("hasRole('SUPERUSER')")
    public ResponseEntity<String> createTenant(
            @Valid @RequestBody CreateTenantRequest request
    ) {
        tenantService.createTenantWithAdmin(request);
        return ResponseEntity.ok("Tenant and Admin created successfully");
    }

    // SUPERUSER can suspend tenant
    @PatchMapping("/{id}/suspend")
    @PreAuthorize("hasRole('SUPERUSER')")
    public ResponseEntity<Void> suspendTenant(@PathVariable Long id) {
        tenantService.suspendTenant(id);
        return ResponseEntity.noContent().build();
    }

    // SUPERUSER can delete tenant (soft delete)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPERUSER')")
    public ResponseEntity<Void> deleteTenant(@PathVariable Long id) {
        tenantService.deleteTenant(id);
        return ResponseEntity.noContent().build();
    }

    // SUPERUSER can view all tenants (companies)
    @GetMapping
    @PreAuthorize("hasRole('SUPERUSER')")
    public ResponseEntity<?> getAllTenants() {
        return ResponseEntity.ok(tenantService.getAllTenants());
    }

}
