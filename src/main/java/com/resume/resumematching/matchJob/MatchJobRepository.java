package com.resume.resumematching.matchJob;

import com.resume.resumematching.matchJob.entity.MatchJob;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchJobRepository extends JpaRepository<MatchJob, Long> {
}

