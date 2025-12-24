package com.resume.resumematching.service;

import com.resume.resumematching.context.TenantContext;
import com.resume.resumematching.user.dto.CreateHrUserRequest;
import com.resume.resumematching.user.dto.UserResponse;
import com.resume.resumematching.tenant.entity.Tenant;
import com.resume.resumematching.user.entity.User;
import com.resume.resumematching.enums.Role;
import com.resume.resumematching.tenant.TenantRepository;
import com.resume.resumematching.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    private final PasswordEncoder passwordEncoder;

//    public List<User> getUsers() {
//
//        Long tenantId = TenantContext.getTenantId();
//
//        // SUPERUSER → all users
//        if (tenantId == null) {
//            return userRepository.findAll();
//        }
//
//        // ADMIN / HR → only own tenant users
//        return userRepository.findByTenantId(tenantId);
//    }

    @Transactional
    public void createHrUser(CreateHrUserRequest request) {

        Long tenantId = TenantContext.getTenantId();

        if (tenantId == null) {
            throw new RuntimeException("SUPERUSER cannot create HR");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        User hr = User.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(Role.HR)
                .tenant(tenant)
                .disabled(false)
                .build();

        userRepository.save(hr);
    }

    public List<UserResponse> getAllUsers() {

        Long tenantId = TenantContext.getTenantId();

        List<User> users;

        if (tenantId == null) {
            // SUPERUSER → see all users
            users = userRepository.findAll();
        } else {
            // ADMIN → only tenant users
            users = userRepository.findByTenantId(tenantId);
        }

        return users.stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getEmail(),
                        user.getRole(),
                        user.isDisabled(),
                        user.getTenant() != null ? user.getTenant().getId() : null,
                        user.getCreatedAt()
                ))
                .toList();
    }

}
