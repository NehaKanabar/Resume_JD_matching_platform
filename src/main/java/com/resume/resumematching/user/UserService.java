package com.resume.resumematching.user;

import com.resume.resumematching.context.TenantContext;
import com.resume.resumematching.user.dto.CreateHrUserRequest;
import com.resume.resumematching.user.dto.UserResponse;
import com.resume.resumematching.tenant.entity.Tenant;
import com.resume.resumematching.user.entity.User;
import com.resume.resumematching.enums.Role;
import com.resume.resumematching.tenant.TenantRepository;
import com.resume.resumematching.user.mapper.UserMapper;
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
    private final UserMapper userMapper;

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

        List<User> users = (tenantId == null)
                ? userRepository.findAll()
                : userRepository.findByTenantId(tenantId);

        return userMapper.toResponseList(users);
    }

}
