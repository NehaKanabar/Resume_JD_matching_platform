package com.resume.resumematching.tenant;


import com.resume.resumematching.tenant.entity.Tenant;
import com.resume.resumematching.enums.TenantStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TenantRepository extends JpaRepository<Tenant, Long> {

    Optional<Tenant> findByName(String name);

    boolean existsByName(String name);

    Optional<Tenant> findByIdAndStatus(Long id, TenantStatus status);

    @Query("""
    SELECT t FROM Tenant t
    WHERE t.status = :status
""")
    List<Tenant> findAllByStatus(@Param("status") TenantStatus status);

}
