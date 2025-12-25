package com.resume.resumematching.tenant;

import com.resume.resumematching.context.TenantContext;
import com.resume.resumematching.tenant.dto.CreateTenantRequest;
import com.resume.resumematching.tenant.dto.TenantResponse;
import com.resume.resumematching.tenant.entity.Tenant;
import com.resume.resumematching.user.entity.User;
import com.resume.resumematching.enums.Role;
import com.resume.resumematching.enums.TenantStatus;
import com.resume.resumematching.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TenantService {

    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Tenant createTenantWithAdmin(CreateTenantRequest request) {

        // SUPERUSER only
        if (TenantContext.getTenantId() != null) {
            throw new RuntimeException("Only SUPERUSER can create tenants");
        }

        // Duplicate checks
        if (tenantRepository.existsByName(request.getTenantName())) {
            throw new RuntimeException("Tenant already exists");
        }

        if (userRepository.existsByEmail(request.getAdminEmail())) {
            throw new RuntimeException("Admin email already in use");
        }

        // Create tenant
        Tenant tenant = Tenant.builder()
                .name(request.getTenantName())
                .status(TenantStatus.ACTIVE)
                .build();

        tenantRepository.save(tenant);

        // Create admin
        User admin = User.builder()
                .email(request.getAdminEmail())
                .passwordHash(passwordEncoder.encode(request.getAdminPassword()))
                .role(Role.ADMIN)
                .tenant(tenant)
                .disabled(false)
                .build();

        userRepository.save(admin);

        return tenant;
    }


    public List<Tenant> getAllActiveTenants() {

        // SUPERUSER only
        if (TenantContext.getTenantId() != null) {
            throw new RuntimeException("Access denied");
        }

        return tenantRepository.findAllByStatus(TenantStatus.ACTIVE);
    }


    public void suspendTenant(Long tenantId) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        tenant.setStatus(TenantStatus.SUSPENDED);
        tenantRepository.save(tenant);
    }

    public void deleteTenant(Long tenantId) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        tenant.setStatus(TenantStatus.DELETED);
        tenantRepository.save(tenant);
    }

    public List<TenantResponse> getAllTenants() {
        return tenantRepository.findAll()
                .stream()
                .map(tenant -> new TenantResponse(
                        tenant.getId(),
                        tenant.getName(),
                        tenant.getStatus().name(),
                        tenant.getCreatedAt().toString()
                ))
                .toList();
    }

}
