package com.resume.resumematching.repository;

import com.resume.resumematching.user.entity.User;
import com.resume.resumematching.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findByTenantId(Long tenantId);

    List<User> findByRole(Role role);

    List<User> findByTenantIdAndRole(Long tenantId, Role role);

    boolean existsByEmail(String email);
}

