package com.resume.resumematching.matchJob;

import com.resume.resumematching.matchJob.entity.MatchResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchResultRepository extends JpaRepository<MatchResult, Long> {

    List<MatchResult> findByMatchJobIdAndTenantIdOrderByOverallScoreDesc(
            Long matchJobId,
            Long tenantId
    );
}

