package com.resume.resumematching.plan;

import com.resume.resumematching.enums.PlanStatus;
import com.resume.resumematching.plan.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    Optional<Plan> findByName(String name);

    Optional<Plan> findTopByNameIgnoreCaseAndStatusOrderByVersionDesc(
            String name,
            PlanStatus status
    );

    Optional<Plan> findByIdAndStatus(Long id, PlanStatus status);

    @Query("""
    SELECT MAX(p.version)
    FROM Plan p
    WHERE UPPER(p.name) = :name
""")
    Optional<Integer> findMaxVersionByName(@Param("name") String name);

    @Query("SELECT p FROM Plan p WHERE p.status = :status")
    List<Plan> findByStatus(@Param("status") PlanStatus status);
}


